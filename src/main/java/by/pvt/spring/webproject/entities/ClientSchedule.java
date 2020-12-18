//package by.pvt.spring.webproject.entities;
//
//import lombok.Data;
//
//import javax.persistence.*;
//import java.util.Date;
//import java.util.List;
//
//@Data
//@Entity
//@Table(name = "client_schedule")
//public class ClientSchedule {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
//	private Integer id;
//
//	@Temporal(TemporalType.DATE)
//	private Date year;
//
//	@ManyToOne
//	@JoinColumn(name = "client_id")
//	private User client;
//
//	@ManyToOne
//	@JoinColumn(name = "schedule_id")
//	private hibernate_test.domain.ScheduleWorkout schedule;
//
//	@OneToMany(mappedBy = "clientChanges", cascade = CascadeType.REMOVE)
//	private List<hibernate_test.domain.ClientChanges> clientChanges;
//}
