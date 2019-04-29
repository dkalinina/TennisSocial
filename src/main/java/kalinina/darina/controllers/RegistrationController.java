package kalinina.darina.controllers;

import kalinina.darina.entities.User;
import kalinina.darina.repositories.UserRepository;
import kalinina.darina.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder encoder;


    @ModelAttribute("roles")
    public void getAllRoles(Model model) {
        model.addAttribute("roles", userRoleRepository.findAll());
    }

    @ModelAttribute("newUser")
    public void newUser(Model model) {
        model.addAttribute("newUser", new User());
    }

    @GetMapping
    public String registrationForm() {
        return "registration";
    }

    @PostMapping
    public String createUser(Model model,
                             @Validated @ModelAttribute("newUser") User newUser,
                             Errors errors) {

        if(errors.hasErrors()) {
            errors.getAllErrors().forEach(err -> System.out.println(err));
            return "registration";
        }

        newUser.setPassword(encoder.encode(newUser.getPassword()));
        System.out.println(newUser);
        userRepository.save(newUser);

        return "redirect:/";
    }
}
