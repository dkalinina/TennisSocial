package kalinina.darina.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kalinina.darina.entities.support.WeekTimetable;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Platform implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
//    @NotNull
    @ManyToOne
    @JsonManagedReference
    private City city;
    @NotNull
    @Size(min = 5, message = "название площадки не менее 5 символов")
    private String name;
    @NotNull
    private double longitude;
    @NotNull
    private double latitude;
    @NotNull
    @Valid
    private WeekTimetable timetable;

    public Platform(String name, double latitude, double longitude, City city) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.timetable = WeekTimetable.openAllTime();
    }


    public boolean isOpenOn(LocalDateTime dateTime) {
        return timetable.isOpenOn(dateTime);
    }


    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof Platform)) return false;

        return id.equals(((Platform) another).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
