package by.pvt.spring.webproject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//error?
    private Date purchase_date;

    private Integer duration;

    private Integer price;

    @OneToOne(mappedBy = "membership")
    @JoinColumn(name = "user_id")
    private User user;
}
