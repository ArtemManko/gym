//package by.pvt.spring.webproject.entities;
//
//import lombok.Data;
//
//import javax.persistence.*;
//
//
//@Data
//@Entity
//@Table(name = "client_changes")
//public class ClientChanges {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
//	private Integer id;
//
//
//	@Column(nullable = false)
//	private Integer weight;
//
//	@Column(nullable = false)
//	private Integer waistSize;
//
//	@ManyToOne
//	@JoinColumn(name = "schedule_id")
//	private ClientSchedule clientChanges;
//}
