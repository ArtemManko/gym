package by.pvt.spring.webproject.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "membership")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date purchase_date;

    private Integer duration;

    private Integer price;

    @Column(name = "payment_id")
    private String paymentId;

    private Boolean active;

    @OneToOne(mappedBy = "membership",fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
