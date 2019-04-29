package kalinina.darina.controllers.basic;

import kalinina.darina.entities.Dialog;
import kalinina.darina.entities.User;
import kalinina.darina.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class WithAnotherUserController extends InnerPageController {
    @ModelAttribute("another")
    public void getAnother(Model model,
                           @RequestParam("user") String login) {
        User another = userRepository.findUserByLogin(login);
        model.addAttribute("another", another);
    }

    @ModelAttribute("dialog")
    public void getDialog(Model model,
                          @ModelAttribute("user") User user,
                          @ModelAttribute("another") User another) {

        Dialog dialog = dialogRepository.findByUsers(user, another);
        if (dialog == null) {
            dialog = new Dialog();
            dialog.setInviter(user);
            dialog.setInvited(another);
            dialog = dialogRepository.save(dialog);
        }
        model.addAttribute("dialog", dialog);
    }
}
