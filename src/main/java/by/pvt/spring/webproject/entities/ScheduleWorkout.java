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


	@ElementCollection(targetClass = Level.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "schedule_level", joinColumns = @JoinColumn(name = "schedule_id"))
	@Enumerated(EnumType.STRING)
	private Set<Level> levels;
	@ElementCollection(targetClass = Day.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "schedule_day", joinColumns = @JoinColumn(name = "schedule_id"))
	@Enumerated(EnumType.STRING)
	private Set<Day> days;


	@ManyToMany( mappedBy = "schedule_workouts")
//	@JoinColumn(name = "user_id")
	private List<User> users;



	//	@OneToMany(mappedBy = "schedules", cascade = CascadeType.REMOVE)
//	private List<hibernate_test.domain.CoachSchedule> coachSchedules;
//
//	@OneToMany(mappedBy = "schedules", cascade = CascadeType.REMOVE)
//	private List<ClientSchedule> clientSchedules;
}
