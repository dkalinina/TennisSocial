package kalinina.darina.interfaces;

import kalinina.darina.validators.DialogMembersValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = DialogMembersValidator.class)
public @interface ValidDialog {
    String message() default "dialog can not be created";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
