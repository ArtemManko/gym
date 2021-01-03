package by.pvt.spring.webproject.entities;

import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "membership_id")
    private Membership membership;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Credentials> credentials = new ArrayList<>();


    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role roles;


    @Column(name = "user_level")
    @Enumerated(EnumType.STRING)
    private Level levels;

    public User() {
    }


    public User(String first_name, String username, String password, Boolean active, String activationCode) {
        this.first_name = first_name;
        this.username = username;
        this.password = password;
        this.active = active;
        this.activationCode = activationCode;
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
        return Collections.singleton(new SimpleGrantedAuthority(getRoles().name()));
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
