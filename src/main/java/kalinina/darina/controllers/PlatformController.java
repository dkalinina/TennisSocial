package kalinina.darina.controllers;

import kalinina.darina.controllers.basic.InnerPageController;
import kalinina.darina.entities.City;
import kalinina.darina.entities.Platform;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collection;

@Controller
@RequestMapping("/platforms")
public class PlatformController extends InnerPageController {
    @ModelAttribute
    public void constants(Model model) {
        model.addAttribute("week", DayOfWeek.values());
    }
    @ModelAttribute("newPlatform")
    public void prepareNewPlatform(Model model) {
        Platform platform = new Platform();
        model.addAttribute("newPlatform", platform);
    }

    @GetMapping
    public String showAllPlatforms() {
        return "platforms";
    }

    @PostMapping("platforms_option")
    public String platformsSelect(Model model, @RequestParam String index){
        City city = cityRepository.findById(index).get();
        model.addAttribute("platforms", platformRepository.findByCity(city));
        return "fragments/platforms_option";
    }

    @PostMapping("platforms_list")
    @ResponseBody
    public Collection<Platform> collectPlatforms(Model model, @RequestParam String index){
        City city = cityRepository.findById(index).get();
        model.addAttribute("city", city);
        return platformRepository.findByCity(city);
    }

    @PostMapping
    public String createNewPlatform(Model model,
                                    @Validated @ModelAttribute("newPlatform") Platform platform,
                                    Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("solveErrors", true);
            return "platforms";
        }

        platform.setCity(cityRepository.findCloseTo(platform.getLatitude(), platform.getLongitude()));

        platformRepository.save(platform);
        return "redirect:platforms";
    }
}
