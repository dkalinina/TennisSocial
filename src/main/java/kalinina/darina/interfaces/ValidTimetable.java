package kalinina.darina.interfaces;

import kalinina.darina.validators.TimetableValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = TimetableValidator.class)
public @interface ValidTimetable {
    String message() default "timetable can not be created";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
