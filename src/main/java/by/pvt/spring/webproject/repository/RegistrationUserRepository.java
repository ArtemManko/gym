package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByActivationCode(String code);
}
