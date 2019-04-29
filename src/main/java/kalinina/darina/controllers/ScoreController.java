package kalinina.darina.controllers;

import kalinina.darina.controllers.basic.InnerPageController;
import kalinina.darina.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/score")
@SessionAttributes({"user"})
public class ScoreController extends InnerPageController {
    @GetMapping
    public String matchesList(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("matches", matchRepository.findByUser(user));
        return "scores";
    }
}
