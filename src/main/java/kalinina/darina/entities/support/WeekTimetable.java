package kalinina.darina.entities.support;

import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Embeddable
public class WeekTimetable implements Serializable {
    @NotNull
    @Valid
    @AttributeOverride(name="openOn", column = @Column(name="monday_open_on"))
    @AttributeOverride(name="closeOn", column = @Column(name="monday_close_on"))
    @AttributeOverride(name="openWholeDay", column = @Column(name="monday_open_whole_day"))
    private Timetable monday;
    @NotNull
    @Valid
    @AttributeOverride(name="openOn", column = @Column(name="tuesday_open_on"))
    @AttributeOverride(name="closeOn", column = @Column(name="tuesday_close_on"))
    @AttributeOverride(name="openWholeDay", column = @Column(name="tuesday_open_whole_day"))
    private Timetable tuesday;
    @NotNull
    @Valid
    @AttributeOverride(name="openOn", column = @Column(name="wednesday_open_on"))
    @AttributeOverride(name="closeOn", column = @Column(name="wednesday_close_on"))
    @AttributeOverride(name="openWholeDay", column = @Column(name="wednesday_open_whole_day"))
    private Timetable wednesday;
    @NotNull
    @Valid
    @AttributeOverride(name="openOn", column = @Column(name="thursday_open_on"))
    @AttributeOverride(name="closeOn", column = @Column(name="thursday_close_on"))
    @AttributeOverride(name="openWholeDay", column = @Column(name="thursday_open_whole_day"))
    private Timetable thursday;
    @NotNull
    @Valid
    @AttributeOverride(name="openOn", column = @Column(name="friday_open_on"))
    @AttributeOverride(name="closeOn", column = @Column(name="friday_close_on"))
    @AttributeOverride(name="openWholeDay", column = @Column(name="friday_open_whole_day"))
    private Timetable friday;
    @NotNull
    @Valid
    @AttributeOverride(name="openOn", column = @Column(name="saturday_open_on"))
    @AttributeOverride(name="closeOn", column = @Column(name="saturday_close_on"))
    @AttributeOverride(name="openWholeDay", column = @Column(name="saturday_open_whole_day"))
    private Timetable saturday;
    @NotNull
    @Valid
    @AttributeOverride(name="openOn", column = @Column(name="sunday_open_on"))
    @AttributeOverride(name="closeOn", column = @Column(name="sunday_close_on"))
    @AttributeOverride(name="openWholeDay", column = @Column(name="sunday_open_whole_day"))
    private Timetable sunday;

    public static WeekTimetable openAllTime() {
        WeekTimetable timetable = new WeekTimetable();
        timetable.monday = Timetable.openDay();
        timetable.tuesday = Timetable.openDay();
        timetable.wednesday = Timetable.openDay();
        timetable.thursday = Timetable.openDay();
        timetable.friday = Timetable.openDay();
        timetable.saturday = Timetable.openDay();
        timetable.sunday = Timetable.openDay();
        return timetable;
    }

    public boolean isOpenOn(LocalDateTime dateTime) {
        DayOfWeek weekday = dateTime.getDayOfWeek();
        LocalTime moment = dateTime.toLocalTime();

        switch (weekday) {
            case MONDAY: return isOpenOn(monday, moment);
            case TUESDAY: return isOpenOn(tuesday, moment);
            case WEDNESDAY: return isOpenOn(wednesday, moment);
            case THURSDAY: return isOpenOn(thursday, moment);
            case FRIDAY: return isOpenOn(friday, moment);
            case SATURDAY: return isOpenOn(saturday, moment);
            case SUNDAY: return isOpenOn(sunday, moment);
        }

        return false;
    }

    private boolean isOpenOn(Timetable weekday, LocalTime time) {
        return weekday != null && weekday.isOpenAt(time);
    }

    public Timetable getDay(DayOfWeek weekday) {
        switch (weekday) {
            case MONDAY: return monday;
            case TUESDAY: return tuesday;
            case WEDNESDAY: return wednesday;
            case THURSDAY: return thursday;
            case FRIDAY: return friday;
            case SATURDAY: return saturday;
            case SUNDAY: return sunday;
            default: return null;
        }
    }
}
