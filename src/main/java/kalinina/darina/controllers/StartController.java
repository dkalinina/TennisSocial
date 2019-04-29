package kalinina.darina.controllers;

import kalinina.darina.controllers.basic.EntitiesListsConroller;
import kalinina.darina.entities.Dialog;
import kalinina.darina.entities.User;
import kalinina.darina.entities.UserRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@SessionAttributes({"user", "roles"})
public class StartController extends EntitiesListsConroller {
    @ModelAttribute("dialog")
    public void prepareDialog(Model model,
                              @ModelAttribute("user") User user) {
        Dialog dialog = new Dialog();
        dialog.setInviter(user);
        model.addAttribute("dialog", dialog);
    }

    @GetMapping
    public String start(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "home";
    }
//
//    @GetMapping(params = {"role"})
//    public String start(Model model,
//                        @RequestParam("role") String role) {
//        UserRole userRole = userRoleRepository.findByRole(role.toUpperCase());
//        model.addAttribute("users", userRepository.findAllByRolesContains(userRole));
//        return "home";
//    }

    @PostMapping
    public String createDialog(Model model,
                               @Validated @ModelAttribute("dialog") Dialog dialog,
                               @ModelAttribute("user") User user,
                               Errors errors) {
        if (errors.hasErrors()) {
            return start(model);
        }

        Dialog alreadyExists = dialogRepository.findByUsers(dialog.getInviter(), dialog.getInvited());
        if (alreadyExists == null) {
            alreadyExists = dialogRepository.save(dialog);
        }

        return "redirect:/dialog?user="+alreadyExists.getAnother(user).getLogin();
    }

}
