package kalinina.darina.validators;

import kalinina.darina.entities.Match;
import kalinina.darina.entities.Platform;
import kalinina.darina.interfaces.ValidMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class MatchImplementabilityValidator implements ConstraintValidator<ValidMatch, Match> {
    public void initialize(ValidMatch constraintAnnotation) {
    }

    @Override
    public boolean isValid(Match match, ConstraintValidatorContext constraintValidatorContext) {
        Platform platform = match.getPlatform();
        LocalDateTime dateTime = match.getDateTime();
        if (!platform.isOpenOn(dateTime)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "в указанный момент площадка закрыта")
                    .addPropertyNode("dateTime").addConstraintViolation();
            return false;
        }
        return true;
    }
}