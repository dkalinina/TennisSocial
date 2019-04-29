package kalinina.darina.controllers.basic;

import kalinina.darina.entities.Dialog;
import kalinina.darina.entities.Match;
import kalinina.darina.entities.Message;
import kalinina.darina.entities.User;
import kalinina.darina.repositories.*;
import kalinina.darina.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Validator;
import java.security.Principal;
import java.util.*;

@Controller
@SessionAttributes({"user", "roles", "cities"})
public class InnerPageController {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserRoleRepository userRoleRepository;
    @Autowired
    protected MessageRepository messageRepository;
    @Autowired
    protected DialogRepository dialogRepository;
    @Autowired
    protected MatchRepository matchRepository;
    @Autowired
    protected FriendsRelationshipRepository friendsRelationshipRepository;
    @Autowired
    protected CityRepository cityRepository;
    @Autowired
    protected PlatformRepository platformRepository;
    @Autowired
    protected SimpMessagingTemplate template;
    @Autowired
    protected ScheduleService scheduleService;
    @Autowired
    protected Validator validator;

    @ModelAttribute("user")
    public void currentUser(Model model, Principal principal) {
        model.addAttribute("user", userRepository.findUserByLogin(principal.getName()));
    }

    @ModelAttribute("unreaded")
    public void countNewMessages(Model model, @ModelAttribute("user") User user) {
        List<Object> unreaded = new LinkedList<>();

        for(Dialog dialog: dialogRepository.findByUser(user)) {
            for(Message message: dialog.getMessages()) {
                if (!message.isReaded() && !message.getAuthor().equals(user)) {
                    unreaded.add(message);
                }
            }
            if (dialog.prevMatchWasDeclinedByAnotherUser(user))
//            if (dialog.prevMatchWasDeclinedByUser(user))
                unreaded.add(dialog);
        }
        for(Match match: matchRepository.findByUser(user)) {
            if (match.isFulfilled() && !match.isScoredBy(user) || !match.isConfirmedBy(user)) {
                unreaded.add(match);
            }
        }

        model.addAttribute("unreaded", unreaded);
    }

    @ModelAttribute("roles")
    public void getAllRoles(Model model) {
        model.addAttribute("roles", userRoleRepository.findAll());
    }

    @ModelAttribute("cities")
    public void getAllCities(Model model) {
        model.addAttribute("cities", cityRepository.findAll());
    }

//    @GetMapping("notification/declinedMatch/remove")
    public void removeNotification(@ModelAttribute("user") User user,
                                   @RequestParam("user") String login) {
        User another = userRepository.findUserByLogin(login);
        Dialog dialog = dialogRepository.findByUsers(user, another);
        removeNotification(dialog);
    }

    public void removeNotification(Dialog dialog) {
        dialog.setPrevMatchWasDeclinedByInviterUser(false);
        dialog.setPrevMatchWasDeclinedByInvitedUser(false);
        dialogRepository.save(dialog);
    }

//    @GetMapping("dialog/remove")
    public void removeDialog(@ModelAttribute("user") User user,
                             @RequestParam("user") String login) {
        User another = userRepository.findUserByLogin(login);
        Dialog dialog = dialogRepository.findByUsers(user, another);
        removeDialog(dialog);
    }

    public void removeDialog(Dialog dialog) {
        dialogRepository.delete(dialog);
    }
}
