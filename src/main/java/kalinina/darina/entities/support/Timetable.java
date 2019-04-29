package kalinina.darina.entities.support;

import kalinina.darina.interfaces.ValidTimetable;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;

@Data
@Embeddable
@ValidTimetable
public class Timetable implements Serializable {
    private LocalTime openOn;
    private LocalTime closeOn;
    private Boolean openWholeDay;

    public static Timetable openDay() {
        Timetable timetable = new Timetable();
        timetable.openOn = null;
        timetable.closeOn = null;
        timetable.openWholeDay = true;
        return timetable;
    }
    public static Timetable closeDay() {
        Timetable timetable = new Timetable();
        timetable.openOn = null;
        timetable.closeOn = null;
        timetable.openWholeDay = false;
        return timetable;
    }

    public boolean isOpenAt(LocalTime time) {
        return openWholeDay || time.isAfter(openOn) && time.isBefore(closeOn);
    }
    public boolean isOpenWholeDay() {
        if (openWholeDay == null || !openWholeDay) return false;
        return true;
    }
    public boolean isClosedWholeDay() {
        return (openWholeDay == null || !openWholeDay) && openOn == null && closeOn == null;
    }
}
