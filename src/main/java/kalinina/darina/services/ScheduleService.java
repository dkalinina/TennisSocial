package kalinina.darina.services;

import kalinina.darina.entities.Dialog;
import kalinina.darina.entities.Match;
import kalinina.darina.repositories.DialogRepository;
import kalinina.darina.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
@Component
public class ScheduleService {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private UpdateSender updateSender;
    private Set<Dialog> haveMatchToday = new HashSet<>();

    public void addDialog(Dialog dialog) {
        if (!haveMatchToday.add(dialog)) {
            haveMatchToday.remove(dialog);
            haveMatchToday.add(dialog);
        }
    }
    public void removeDialog(Dialog dialog) {
        haveMatchToday.remove(dialog);
    }

    @Scheduled(initialDelay=5000, fixedRate=24*60*60*1000)
    public void todaysMatches() {
        Collection<Match> matches = matchRepository.findByDate(LocalDate.now());
        Collection<Dialog> dialogs = dialogRepository.findByMatchIn(matches);
        haveMatchToday.addAll(dialogs);
    }

    @Scheduled(cron = "0 * * * * *")
    public void finishMatches() {
        LocalTime now = LocalTime.now();
        Iterator<Dialog> iterator = haveMatchToday.iterator();
        while (iterator.hasNext()) {
            Dialog dialog = iterator.next();
            Match match = dialog.getMatch();

            if (match == null) {
                iterator.remove();
            } else {
                LocalTime matchTime = match.getDateTime().toLocalTime();
                if (matchTime.getHour() == now.getHour() && matchTime.getMinute() == now.getMinute()) {
                    boolean confInviter = match.isConfirmedByInviter();
                    boolean confInvited = match.isConfirmedByInvited();

                    dialog.setPrevMatchWasDeclinedByInviterUser(!confInviter);
                    dialog.setPrevMatchWasDeclinedByInvitedUser(!confInvited);
                    dialog.setMatch(null);
                    dialog = dialogRepository.save(dialog);

                    if (confInviter && confInvited) {
                        match.setFulfilled(true);
                        matchRepository.updateFulfilled(match.getId(), true);
                    } else {
                        matchRepository.delete(match);
                    }

                    updateSender.sendNewDialogReactionViews(dialog);

                    iterator.remove();
                }
            }
        }
    }
}
