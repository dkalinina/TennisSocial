package kalinina.darina.repositories;

import kalinina.darina.entities.Match;
import kalinina.darina.entities.User;
import kalinina.darina.entities.support.Score;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public interface MatchRepository extends CrudRepository<Match, Long> {
    Collection<Match> findByInviter(User user);
    Collection<Match> findByInvited(User user);
    default Collection<Match> findByUser(User user) {
        Collection<Match> result = findByInviter(user);
        result.addAll(findByInvited(user));
        return result;
    }
    Collection<Match> findByDateTimeBetween(LocalDateTime start, LocalDateTime finish);
    default Collection<Match> findByDate(LocalDate date) {
        return findByDateTimeBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Modifying
    @Transactional
    @Query("UPDATE Match SET fulfilled = ?2 WHERE id = ?1")
    void updateFulfilled(Long matchId, boolean fulfilled);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Match m SET m.inviterScore = :inviterScore WHERE m.id = :matchId")
//    void updateInviterScore(@Param("matchId") Long matchId, @Param("inviterScore") Score score);
//    @Modifying
//    @Transactional
//    @Query("UPDATE Match m SET m.invitedScore = :invitedScore WHERE m.id = :matchId")
//    void updateInvitedScore(@Param("matchId") Long matchId, @Param("invitedScore") Score score);
//    default void scoreByUser(Match match, User user, Score score) {
//        if (match.isInviter(user)) updateInviterScore(match.getId(), score);
//        else if (match.isInvited(user)) updateInvitedScore(match.getId(), score);
//    }
}
