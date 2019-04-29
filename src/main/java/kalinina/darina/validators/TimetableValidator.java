package kalinina.darina.validators;

import kalinina.darina.entities.support.Timetable;
import kalinina.darina.interfaces.ValidTimetable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class TimetableValidator implements ConstraintValidator<ValidTimetable, Timetable> {
    public void initialize(ValidTimetable constraintAnnotation) {
    }

    @Override
    public boolean isValid(Timetable timetable, ConstraintValidatorContext constraintValidatorContext) {
        Boolean openWholeDay = timetable.getOpenWholeDay();
        LocalTime open = timetable.getOpenOn();
        LocalTime close = timetable.getCloseOn();

        if (openWholeDay == null) {
            if (open == null || close == null) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "необходимо указать часы работы")
                        .addPropertyNode("openOn").addConstraintViolation();
                return false;
            }
            if (!open.isBefore(close)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "время открытия должно быть раньше времени закрытия")
                        .addPropertyNode("openOn").addConstraintViolation();
                return false;
            }
        } else if (open != null || close != null) {
            String message = "";
            if (openWholeDay) message = "указано расписание для площадки, открытой целый день";
            else message = "указано расписание для площадки, закрытой целый день";
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("openOn").addConstraintViolation();
            return false;
        }
        return true;
    }
}
