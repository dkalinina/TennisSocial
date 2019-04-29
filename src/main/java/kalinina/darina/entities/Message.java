package kalinina.darina.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    @NotNull
    @ManyToOne
    private User author;
    @NotNull
    @ManyToOne
    @JsonIgnore
    private Dialog dialog;
    private LocalDateTime dateTime;
    @NotNull
    @Size(min = 1)
    private String message;
    private boolean readed = false;

    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof Message)) return false;

        return id.equals(((Message) another).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public String toString() {
        return ""+author.getLogin()+": \""+message+"\" at "+dateTime;
    }
}
