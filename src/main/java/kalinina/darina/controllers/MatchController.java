package kalinina.darina.controllers;

import kalinina.darina.controllers.basic.WithAnotherUserController;
import kalinina.darina.entities.*;
import kalinina.darina.services.UpdateSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/offerMatch")
@SessionAttributes({"user", "roles"})
public class MatchController extends WithAnotherUserController {
    @Autowired
    private UpdateSender updateSender;

    @ModelAttribute("match")
    public void prepareMatch(Model model,
                             @ModelAttribute("dialog") Dialog dialog,
                             @ModelAttribute("user") User user,
                             @ModelAttribute("another") User another) {
        Match match = dialog.getMatch();
        if (match == null) {
            match = new Match();
            match.setInviter(user);
            match.setInvited(another);
            match.setConfirmedByInvited(false);
            match.setConfirmedByInviter(true);
        }
        model.addAttribute("match", match);
    }
    @ModelAttribute
    public void choosedValues(Model model,
                              @ModelAttribute("match") Match match) {
        if (match != null) {
            model.addAttribute("dateTime", match.getDateTime());
            Platform platform = match.getPlatform();
            if (platform != null) {
                model.addAttribute("platform", platform);
                City city = platform.getCity();
                if (city != null) {
                    model.addAttribute("platforms", platformRepository.findByCity(city));
                }
            }
        }
    }

    @GetMapping
    public String createMatch(@ModelAttribute("match") Match match,
                              @ModelAttribute("another") User another) {
        if (match.isConfirmedBy(another))
            return "redirect:/dialog?user="+another.getLogin();
        return "newMatch";
    }

    @PostMapping()
    public String offerMatch(@ModelAttribute("dialog") Dialog dialog,
                             @ModelAttribute("user") User user,
                             @RequestParam("user") String login,
                             @Validated @ModelAttribute("match") Match match,
                             Errors errors) {
        if (errors.hasErrors()) {
            return "newMatch";
        }

        User another = userRepository.findUserByLogin(login);

        if (match == null) throw new RuntimeException("try to set null match");

        Match prevVersion = dialog.getMatch();
        if (prevVersion != null && !match.equals(prevVersion)) {
            throw new RuntimeException("try to set two matches");
        }

        match.setInviter(user);
        match.setInvited(dialog.getAnother(user));
        match.setFulfilled(false);
        match.setConfirmedByInviter(true);
        match.setConfirmedByInvited(false);
        match = matchRepository.save(match);

        dialog.setMatch(match);
        dialog = dialogRepository.save(dialog);

        if (match.getDateTime().toLocalDate().equals(LocalDate.now())) {
            scheduleService.addDialog(dialog);
        }

        updateSender.sendNewDialogReactionViews(dialog);

        return "redirect:/dialog?user="+another.getLogin();
    }
}
