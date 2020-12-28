package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.Credentials;
import by.pvt.spring.webproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

}