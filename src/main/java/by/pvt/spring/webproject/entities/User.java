package by.pvt.spring.webproject.entities;

import by.pvt.spring.webproject.entities.enums.Role;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Enter your name!")
    @Length( max = 20, message = "Name too long!")
    private String first_name;
    private String last_name;

//    @Temporal(TemporalType.DATE)
    private Date birthday;


//    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    private Boolean gender;

//    @Column(nullable = false)
    private String password;

//    @Column(nullable = false)
    @Transient
    private String password2;
    @Transient
    private String password3;

    private Boolean active;

//    @Email
//    @Column(nullable = false, unique = true)
    private String email;
    private String activationCode;

//    @Column(nullable = false)
    private String phone_number;

//    @Length(max = 30)
//    @Column(nullable = false)
    private String country;

//    @Length(max = 30)
//    @Column(nullable = false)
    private String city;

//    @Length(max = 30)
//    @Column(nullable = false)
    private String street;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
