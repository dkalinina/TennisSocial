package kalinina.darina.repositories;

import kalinina.darina.entities.Dialog;
import kalinina.darina.entities.Match;
import kalinina.darina.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

public interface DialogRepository extends CrudRepository<Dialog, Long> {
    Collection<Dialog> findByInviter(User user);
    Collection<Dialog> findByInvited(User user);
    Dialog findByInviterAndInvited(User user1, User user2);
    Collection<Dialog> findByMatch(Match match);
    Collection<Dialog> findByMatchIn(Collection<Match> matches);

    default Dialog findByUsers(User user1, User user2) {
        Dialog d1 = findByInviterAndInvited(user1, user2);

        if (d1 != null) return d1;
        else return findByInviterAndInvited(user2, user1);
    }
    default Collection<Dialog> findByUser(User user) {
        Collection<Dialog> result = findByInviter(user);
        result.addAll(findByInvited(user));
        return result;
    }
}
