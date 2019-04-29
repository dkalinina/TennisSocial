package kalinina.darina.repositories;

import kalinina.darina.entities.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRole.Role> {
    UserRole findByRole(UserRole.Role role);
    default UserRole findByRole(String roleName) {
        try {
            UserRole.Role role = UserRole.Role.valueOf(roleName);
            return findByRole(role);
        } catch (Exception e) {
            return null;
        }
    }
}
