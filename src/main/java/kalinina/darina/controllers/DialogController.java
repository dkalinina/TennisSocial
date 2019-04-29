package kalinina.darina.controllers;

import kalinina.darina.controllers.basic.WithAnotherUserController;
import kalinina.darina.entities.*;
import kalinina.darina.services.UpdateSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Controller
@RequestMapping("/dialog")
@SessionAttributes({"user"})
public class DialogController extends WithAnotherUserController {
    @Autowired
    private UpdateSender updateSender;

    @ModelAttribute("messages")
    public void getMessages(Model model,
                            @ModelAttribute("dialog") Dialog dialog,
                            @ModelAttribute("user") User user) {
        Collection<Message> messages = dialog.getMessages();
        model.addAttribute("messages", dialog.getMessages());

        for (Message message: messages) {
            if (!message.getAuthor().equals(user) && !message.isReaded()) {
                message.setReaded(true);
                messageRepository.save(message);
            }
        }
    }

    @GetMapping
    public String showDialog(Model model,
                             @ModelAttribute("dialog") Dialog dialog) {
        Match match = dialog.getMatch();
        if (match != null) {
            Platform platform = match.getPlatform();
            model.addAttribute("platform", platform);
            model.addAttribute("platforms", platformRepository.findByCity(platform.getCity()));
        }
//        removeNotification(dialog);
        return "dialog";
    }

    @PostMapping("/printMessage")
    @ResponseBody
    private String printCurrentMessage() {
        return "fragments/oneMessage";
    }

    @PostMapping
    public String writeMessage(Model model,
                               @ModelAttribute("dialog") Dialog dialog,
                               @ModelAttribute("user") User user,
                               @ModelAttribute("another") User another,
                               @RequestParam String message) {
        Message newMessage = new Message();
        newMessage.setMessage(message);
        newMessage.setDialog(dialog);
        newMessage.setAuthor(user);

        Set errors = validator.validate(newMessage);

        if (!errors.isEmpty()) {
            return "";
        }

        if (dialog.getMatch() != null) {
            model.addAttribute("message", newMessage);
            return "fragments/oneMessage_error";
        }

        newMessage.setDateTime(LocalDateTime.now());
        newMessage.setDialog(dialog);
        dialog.getMessages().add(newMessage);
        newMessage = messageRepository.save(newMessage);
        dialogRepository.save(dialog);

        updateSender.sendNewMessageViewToUser(newMessage, another);

        model.addAttribute("message", newMessage);
        return "fragments/oneMessage";
    }

    @GetMapping("/offerMatch")
    public String offerMatch(@ModelAttribute("another") User another) {
        return "redirect:/offerMatch?user="+another.getLogin();
    }

    @GetMapping("/declineMatch")
    public String declineMatch(@ModelAttribute("dialog") Dialog dialog,
                               @ModelAttribute("user") User user,
                               @ModelAttribute("another") User another) {
        Match match = dialog.getMatch();
        dialog.setMatch(null);
        dialogRepository.save(dialog);

        if (match != null) {
            if (match.isConfirmedBy(match.getAnother(user))) {
                if(dialog.getInviter().equals(user)) dialog.setPrevMatchWasDeclinedByInviterUser(true);
                else if(dialog.getInvited().equals(user)) dialog.setPrevMatchWasDeclinedByInvitedUser(true);
                dialogRepository.save(dialog);
            }
            matchRepository.delete(match);

            if (match.getDateTime().toLocalDate().equals(LocalDate.now())) {
                scheduleService.removeDialog(dialog);
            }
        }

        updateSender.sendNewDialogReactionViews(dialog);

        return "redirect:/dialog?user="+another.getLogin();
    }

    @GetMapping("/confirmMatch")
    public String confirmMatch(@ModelAttribute("dialog") Dialog dialog,
                               @ModelAttribute("user") User user,
                               @ModelAttribute("another") User another) {
        Match match = dialog.getMatch();

        if (match.isInviter(user)) match.setConfirmedByInviter(true);
        else if (match.isInvited(user)) match.setConfirmedByInvited(true);

        matchRepository.save(match);

        if (match.getDateTime().toLocalDate().equals(LocalDate.now())) {
            scheduleService.addDialog(dialog);
        }

        updateSender.sendNewDialogReactionViews(dialog);

        return "redirect:/dialog?user="+another.getLogin();
    }
}
