package by.pvt.spring.webproject.entities;

import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
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
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    @NotBlank(message = "Enter your name!")
//    @Length( max = 20, message = "Name too long!")
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_schedule_workouts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private List<ScheduleWorkout> schedule_workouts = new ArrayList<>();

    public User() {

    }

//    public void setSchedule_workouts(List<ScheduleWorkout> schedule_workouts) {
//        if (schedule_workouts != null) {
//            schedule_workouts.forEach(a ->{ a.setUser(this);
//            });
//        }
//    }

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @ElementCollection(targetClass = Level.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_level", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Level> levels;

    public User(String first_name, String username, String password, Boolean active, String activationCode) {
        this.first_name = first_name;
        this.username = username;
        this.password = password;
        this.active = active;
        this.activationCode = activationCode;
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
