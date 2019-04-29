package kalinina.darina.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kalinina.darina.interfaces.ValidDialog;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

@Entity
@Data
@ValidDialog
public class Dialog implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne
    @JsonManagedReference
    private User inviter;
    @NotNull
    @ManyToOne
    @JsonManagedReference
    private User invited;
    @OneToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    private Collection<Message> messages = new LinkedList<>();
    @OneToOne
    @JsonManagedReference
    private Match match;
    private Boolean prevMatchWasDeclinedByInviterUser = false;
    private Boolean prevMatchWasDeclinedByInvitedUser = false;

    public User getAnother(User user) {
        if (inviter.equals(user)) return invited;
        if (invited.equals(user)) return inviter;
        else return null;
    }

    public boolean prevMatchWasDeclinedByUser(User user) {
        if (inviter.equals(user)) return prevMatchWasDeclinedByInviterUser;
        else if (invited.equals(user)) return prevMatchWasDeclinedByInvitedUser;
        else return false;
    }
    public boolean prevMatchWasDeclinedByAnotherUser(User user) {
        return prevMatchWasDeclinedByUser(getAnother(user));
    }

    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof Dialog)) return false;

        return id.equals(((Dialog) another).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
