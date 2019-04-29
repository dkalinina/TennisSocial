package kalinina.darina.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kalinina.darina.entities.support.Score;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class Match implements Serializable {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    @NotNull
    @ManyToOne
    @JsonManagedReference
    private User inviter;
    @NotNull
    @ManyToOne
    @JsonManagedReference
    private User invited;
    @NotNull
    @ManyToOne
    @JsonManagedReference
    private Platform platform;
    @NotNull
    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private boolean confirmedByInviter = false;
    private boolean confirmedByInvited = false;
    private boolean fulfilled = false;
    private int resultScore;
    @AttributeOverride(name="score", column = @Column(name="inviter_score"))
    private Score inviterScore;
    @AttributeOverride(name="score", column = @Column(name="invited_score"))
    private Score invitedScore;

    public boolean isInviter(User user) {
        return user.equals(inviter);
    }
    public boolean isInvited(User user) {
        return user.equals(invited);
    }
    public boolean isConfirmedBy(User user) {
        if (isInviter(user)) return confirmedByInviter;
        if (isInvited(user)) return confirmedByInvited;
        return false;
    }
    public void confirmBy(User user) {
        if (isInviter(user)) setConfirmedByInviter(true);
        if (isInvited(user)) setConfirmedByInvited(true);
    }
    public boolean isScoredBy(User user) {
        if (isInviter(user)) return inviterScore != null;
        if (isInvited(user)) return invitedScore != null;
        return false;
    }
    public void ScoredBy(User user, Score score) {
        if (isInviter(user)) setInviterScore(score);
        if (isInvited(user)) setInvitedScore(score);
    }

    public User getAnother(User user) {
        if (inviter.equals(user)) return invited;
        else return inviter;
    }

    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof Match)) return false;

        return id.equals(((Match) another).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}