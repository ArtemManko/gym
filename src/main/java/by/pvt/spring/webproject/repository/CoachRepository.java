package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CoachRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

}