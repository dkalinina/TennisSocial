package kalinina.darina.services;

import kalinina.darina.entities.*;
import kalinina.darina.repositories.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class UpdateSender {
    @Autowired
    private ViewResolver viewResolver;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private SimpMessagingTemplate template;
    private Locale locale = new Locale("ru", "RU");

    @Data
    @AllArgsConstructor
    public static class NotificationsAndUpdates {
        private User author;
        private String htmlBlock;
        private String notification;
    }

    private String render(String resolveViewName,
                          Map<String, Object> modelInfo) throws Exception {
        View resolvedView = null;
        try {
            resolvedView = viewResolver.resolveViewName(resolveViewName, locale);
        } catch (IllegalStateException e) {
            for (ViewResolver resolver : ((ContentNegotiatingViewResolver) viewResolver).getViewResolvers()) {
                if (resolver instanceof ThymeleafViewResolver) {
                    resolvedView = resolver.resolveViewName(resolveViewName, locale);
                    break;
                }
            }
        }
        MockHttpServletResponse mockResp = new MockHttpServletResponse();
        MockHttpServletRequest mockReq = new MockHttpServletRequest();
        resolvedView.render(modelInfo, mockReq, mockResp);
        return mockResp.getContentAsString();
    }

    private NotificationsAndUpdates dialogReactionViewForUser(Dialog dialog,
                                                              User user,
                                                              User another) throws Exception {
        Map<String, Object> modelInfo = new HashMap<>();
        String resolveViewName;

        Match match = dialog.getMatch();

        if (match == null) {
            resolveViewName = "fragments/newMessageArea";
        } else {
            resolveViewName = "fragments/matchInfoAndButtons";
            modelInfo.put("cities", cityRepository.findAll());
            modelInfo.put("dialog", dialog);
            Platform platform = match.getPlatform();
            if (platform != null) {
                modelInfo.put("platform", platform);
                City city = platform.getCity();
                if (city != null) {
                    modelInfo.put("platforms", platformRepository.findByCity(city));
                }
            }
        }

        modelInfo.put("user", user);
        modelInfo.put("another", another);
        modelInfo.put("notification_dialog", dialog);

        return new NotificationsAndUpdates(
                another,
                render(resolveViewName, modelInfo),
                render("fragments/notificationLine", modelInfo));
    }

    public boolean sendNewDialogReactionViewToUser(Dialog dialog,
                                                   User user,
                                                   User another) {
        try {
            NotificationsAndUpdates view = dialogReactionViewForUser(
                    dialog,
                    user,
                    another);
            template.convertAndSendToUser(user.getLogin(), "/queue/matches/", view);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean sendNewDialogReactionViews(Dialog dialog) {
        User inviter = dialog.getInviter();
        User invited = dialog.getInvited();
        boolean sendedToInviter = sendNewDialogReactionViewToUser(dialog, inviter, invited);
        boolean sendedToInvited = sendNewDialogReactionViewToUser(dialog, invited, inviter);

        return sendedToInviter && sendedToInvited;
    }



    private NotificationsAndUpdates newMessageViewForUser(Message newMessage,
                                                          User user) throws Exception {
        Map<String, Object> modelInfo = new HashMap<>();
        String resolveViewName = "fragments/oneMessage";

        modelInfo.put("message", newMessage);
        modelInfo.put("user", user);
        modelInfo.put("notification_message", newMessage);

        return new NotificationsAndUpdates(
                newMessage.getAuthor(),
                render(resolveViewName, modelInfo),
                render("fragments/notificationLine", modelInfo));
    }

    public boolean sendNewMessageViewToUser(Message newMessage,
                                            User user) {
        try {
            NotificationsAndUpdates view = newMessageViewForUser(
                    newMessage,
                    user);
            template.convertAndSendToUser(user.getLogin(), "/queue/messages/", view);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
