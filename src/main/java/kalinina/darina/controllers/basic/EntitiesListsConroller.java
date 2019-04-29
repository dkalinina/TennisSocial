package kalinina.darina.controllers.basic;

import kalinina.darina.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@SessionAttributes({"user"})
public class EntitiesListsConroller extends InnerPageController {
    @ModelAttribute("companions")
    public void findAllDialogs(Model model, @ModelAttribute("user") User user) {
        Set<User> companions = new HashSet<>();
        dialogRepository.findByUser(user).forEach(dialog -> {
            companions.add(dialog.getAnother(user));
        });
        model.addAttribute("companions", companions);
    }

    @ModelAttribute("friends")
    public void findFriends(Model model, @ModelAttribute("user") User user) {
        Set<User> friends = new HashSet<>();
        friendsRelationshipRepository.findByUserConfirmed(user).forEach(rel -> {
            friends.add(rel.getAnother(user));
        });
        model.addAttribute("friends", friends);
    }

    @ModelAttribute("almostFriends")
    public void findAlmostFriends(Model model, @ModelAttribute("user") User user) {
        Set<User> almostFriends = new HashSet<>();
        friendsRelationshipRepository.findByUserShouldBeConfirmed(user).forEach(rel -> {
            almostFriends.add(rel.getAnother(user));
        });
        model.addAttribute("almostFriends", almostFriends);
    }
}
