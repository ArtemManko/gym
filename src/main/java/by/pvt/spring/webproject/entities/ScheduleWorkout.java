package by.pvt.spring.webproject.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "schedule_workout")
public class ScheduleWorkout {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

//	@Column(nullable = false)
//	private String client_level;

//	public ScheduleWorkout(String client_level) {
//		this.client_level = client_level;
//	}


//	@Column(nullable = false)
	private String day_of_week;

//	@Column(nullable = false)
	private String start_time;

//	@Column(nullable = false)
	private String end_time;

//	@ManyToOne( fetch = FetchType.EAGER)
//	@JoinColumn(name = "user_id")
//	private User user_schedule;

//	public ScheduleWorkout() {
//
//	}


	//	@OneToMany(mappedBy = "schedules", cascade = CascadeType.REMOVE)
//	private List<hibernate_test.domain.CoachSchedule> coachSchedules;
//
//	@OneToMany(mappedBy = "schedules", cascade = CascadeType.REMOVE)
//	private List<ClientSchedule> clientSchedules;
}
