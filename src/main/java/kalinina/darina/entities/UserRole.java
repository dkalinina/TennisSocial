package kalinina.darina.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserRole implements Serializable {
    public enum Role implements Serializable {
        PLAYER, STUDENT, TEACHER, ADMIN
    }

    @Id
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }
    public boolean isNotAdmin() {
        return !role.equals(Role.ADMIN);
    }

    public boolean isAlternative() {
        return role.equals(Role.STUDENT) || role.equals(Role.TEACHER);
    }

    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof UserRole)) return false;
        return role.equals(((UserRole) another).role);
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }
}
