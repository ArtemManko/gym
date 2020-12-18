//package by.pvt.spring.webproject.entities;
//
//
//import lombok.Data;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Data
//@Entity
//@Table(name = "credentials")
//public class Credentials {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
//	private Integer id;
//
//	@Column(nullable = false)
//	private String password;
//
//	@Column(nullable = false)
//	private Boolean active;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date createDate;
//
//	@ManyToOne
//	@JoinColumn(name = "user_id")
//	private User ownerUser;
//
//}
