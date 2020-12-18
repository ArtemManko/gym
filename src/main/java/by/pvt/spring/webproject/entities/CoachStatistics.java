//package by.pvt.spring.webproject.entities;
//
//import lombok.Data;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@Table(name = "coach_statistics")
//public class CoachStatistics {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Integer id;
//
//
//    @Column(nullable = false)
//    private Integer goldMedals;
//
//    @Column(nullable = false)
//    private Integer silverMedals;
//
//    @Column(nullable = false)
//    private Integer bronzeMedals;
//
//    @ManyToOne
//    @JoinColumn(name = "coach_id")
//    private CoachStatistics statistics;
//}
