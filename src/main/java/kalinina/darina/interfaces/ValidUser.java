package kalinina.darina.interfaces;

import kalinina.darina.validators.UserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = UserValidator.class)
public @interface ValidUser {
    String message() default "user can not be created";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
