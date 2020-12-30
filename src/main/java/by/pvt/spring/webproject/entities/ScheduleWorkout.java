package by.pvt.spring.webproject.entities;

import by.pvt.spring.webproject.entities.enums.Day;
import by.pvt.spring.webproject.entities.enums.Level;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "schedule_workout")
public class ScheduleWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //	@Column(nullable = false)
    private String start_end_time;


    //    @ElementCollection(targetClass = Level.class, fetch = FetchType.EAGER)
    @Column(name = "schedule_level")
    @Enumerated(EnumType.STRING)
    private Level levels;

    //    @ElementCollection(targetClass = Day.class, fetch = FetchType.EAGER)
    @Column(name = "schedule_day")
    @Enumerated(EnumType.STRING)
    private Day days;

    @ManyToMany(mappedBy = "schedule_workouts")
    private List<User> users;
}
