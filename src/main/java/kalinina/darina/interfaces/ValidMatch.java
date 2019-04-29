package kalinina.darina.interfaces;

import kalinina.darina.validators.MatchImplementabilityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = MatchImplementabilityValidator.class)
public @interface ValidMatch {
    String message() default "match can not be created";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
