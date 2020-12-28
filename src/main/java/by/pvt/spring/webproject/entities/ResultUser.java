package by.pvt.spring.webproject.entities;


import by.pvt.spring.webproject.entities.User;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "result_user")
public class ResultUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer weight;

    private Integer benchPress;

    private Integer pullUp;

    private Integer pushUp;

    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
