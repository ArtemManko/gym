package by.pvt.spring.webproject.entities;

import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    @Length( max = 20")
    private String first_name;
    private String last_name;


    private Date birthday;

    //    @Column(nullable = false)
    private String username;

    private Boolean gender;

    //    @Column(nullable = false)
    private String password;

    @Transient
    private String password2;
    @Transient
    private String password3;

    private Boolean active;


    private String email;
    private String activationCode;


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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_schedule_workouts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private List<ScheduleWorkout> schedule_workouts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Credentials> credentials = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ResultUser> resultUsers = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ElementCollection(targetClass = Level.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_level", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Level> levels;

    public User() {
    }


    public User(String first_name, String username, String password, Boolean active, String activationCode) {
        this.first_name = first_name;
        this.username = username;
        this.password = password;
        this.active = active;
        this.activationCode = activationCode;
    }

    public void setResultUsers(List<ResultUser> resultUsers) {
        if (resultUsers != null) {
            resultUsers.forEach(a -> {
                a.setUser(this);
            });
        }
        this.resultUsers = resultUsers;
    }

    public void setCredentials(List<Credentials> credentials) {
        if (credentials != null) {
            credentials.forEach(a -> {
                a.setUser(this);
            });
        }
        this.credentials = credentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return getActive();
    }
}
