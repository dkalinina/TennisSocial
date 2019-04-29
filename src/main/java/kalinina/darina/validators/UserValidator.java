package kalinina.darina.validators;

import kalinina.darina.entities.User;
import kalinina.darina.interfaces.ValidUser;
import kalinina.darina.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidator implements ConstraintValidator<ValidUser, User> {
    @Autowired
    private UserRepository userRepository;

    public void initialize(ValidUser constraintAnnotation) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;

//        for preloader only
        if (userRepository == null) {return true;}

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "введенные пароли не совпадают")
                    .addPropertyNode("confirmPassword").addConstraintViolation();

            valid = false;
        }
        if (userRepository.findUserByLogin(user.getLogin()) != null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "пользователь с таким логином уже существует")
                    .addPropertyNode("login").addConstraintViolation();
            valid = false;
        }

        return valid;


//        Platform platform = match.getPlatform();
//        LocalDateTime dateTime = match.getDateTime();
//        return platform.isOpenOn(dateTime);
    }
}
