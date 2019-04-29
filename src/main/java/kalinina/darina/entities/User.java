package kalinina.darina.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kalinina.darina.interfaces.ValidUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@ValidUser
public class User implements Serializable, UserDetails {
    @Id
    @NotNull
    @Size(min = 3, message = "длина логина не менее 3 символов")
    private String login;
    @NotNull
    @Size(min = 6, message = "длина пароля не менее 6 символов")
    @JsonIgnore
    private String password;
    @Transient
    @JsonIgnore
    private String confirmPassword;
    @ManyToOne
    @JsonManagedReference
    private City currentCity;
    private int playerScore = 0;
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @Size(min = 1, message = "необходимо выбрать хотя бы одну роль")
    @JsonIgnore
    private Collection<UserRole> roles = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
//    @JsonBackReference
    @JsonIgnore
    private Set<Match> matches = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER)
//    @JsonBackReference
    @JsonIgnore
    private Set<Dialog> dialogs = new HashSet<>();

    public User(String login, String password, Collection<UserRole> roles) {
        this.login = login;
        this.password = password;
        this.confirmPassword = password;
        this.roles.addAll(roles);
    }
    public User(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.confirmPassword = password;
        this.roles.add(role);
    }

    public boolean hasRole(String roleName) {
        UserRole.Role role = UserRole.Role.valueOf(roleName.toUpperCase());
        return role != null && hasRole(role);
    }
    public boolean hasRole(UserRole.Role role) {
        for (UserRole r: roles) {
            if (r.getRole() == role) return true;
        }
        return false;
    }

    public void setRoles(Collection<UserRole> roles) {
        this.roles = roles;
        this.roles.remove(null);
    }

    @Override
    public boolean equals(Object another) {
        if (another == null || !(another instanceof User)) return false;
        else return login.equals(((User) another).login);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().toString()));
        }
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return login;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
