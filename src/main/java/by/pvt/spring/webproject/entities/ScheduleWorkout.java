package by.pvt.spring.webproject.entities;

import by.pvt.spring.webproject.entities.enums.Day;
import by.pvt.spring.webproject.entities.enums.Level;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "schedule_workout")
public class ScheduleWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String start_end_time;

    @Column(name = "schedule_level")
    @Enumerated(EnumType.STRING)
    private Level levels;

    @ElementCollection(targetClass = Day.class, fetch = FetchType.EAGER)
    @Column(name = "schedule_day")
    @Enumerated(EnumType.STRING)
    private Set<Day> days;

    @ManyToMany(mappedBy = "schedule_workouts", fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleWorkout)) return false;
        ScheduleWorkout that = (ScheduleWorkout) o;
        return getDays().equals(that.getDays()) &&
                getStart_end_time().equals(that.getStart_end_time()) &&
                getLevels() == that.getLevels();
    }

}
