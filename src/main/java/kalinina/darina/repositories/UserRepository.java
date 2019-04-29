package kalinina.darina.repositories;

import kalinina.darina.entities.User;
import kalinina.darina.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public interface UserRepository extends CrudRepository<User, String> {
    User findUserByLogin(String login) throws UsernameNotFoundException;
    Collection<User> findAllByRolesContains(UserRole role);
}
