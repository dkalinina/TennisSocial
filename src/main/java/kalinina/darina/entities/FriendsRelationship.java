package kalinina.darina.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class FriendsRelationship {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    @NotNull
    @ManyToOne
    @JsonManagedReference
    private User from;
    @NotNull
    @ManyToOne
    @JsonManagedReference
    private User to;
    private boolean confirmedFrom = false;
    private boolean confirmedTo = false;

    public boolean shouldBeConfirmedBy(User user) {
        if (user.equals(to)) return !confirmedTo;
        else if (user.equals(from)) return !confirmedFrom;
        return false;
    }

    public void confirmBy(User user) {
        if (user.equals(to)) setConfirmedTo(true);
        else if (user.equals(from)) setConfirmedFrom(true);
    }

    public User getAnother(User user) {
        if (to.equals(user)) return from;
        if (from.equals(user)) return to;
        else return null;
    }

    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof FriendsRelationship)) return false;

        return id.equals(((FriendsRelationship) another).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
