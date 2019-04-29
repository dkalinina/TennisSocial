package kalinina.darina.validators;

import kalinina.darina.entities.Dialog;
import kalinina.darina.entities.User;
import kalinina.darina.interfaces.ValidDialog;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DialogMembersValidator implements ConstraintValidator<ValidDialog, Dialog> {
    public void initialize(ValidDialog constraintAnnotation) {
    }

    @Override
    public boolean isValid(Dialog dialog, ConstraintValidatorContext constraintValidatorContext) {
        User inviter = dialog.getInviter();
        User invited = dialog.getInvited();
        if (inviter.equals(invited)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "inviter and invited are the same person")
                    .addPropertyNode("invited").addConstraintViolation();
            return false;
        }

        return true;
    }
}
