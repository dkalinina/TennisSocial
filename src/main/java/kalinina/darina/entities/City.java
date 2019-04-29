package kalinina.darina.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class City implements Serializable {
    @Id
    @NotNull
    @Pattern(regexp = "(?:[0-9]{6})(?:[-|—| ](?:[0-9]{6}))?")
    private String index;
    @NotNull
    @Size(min=3)
    private String name;
    @OneToMany
    @JsonBackReference
    private Set<Platform> platforms = new HashSet<>();
    private double longitude;
    private double latitude;

    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof City)) return false;

        return index.equals(((City) another).index);
    }

    @Override
    public int hashCode() {
        return index.hashCode();
    }
}
