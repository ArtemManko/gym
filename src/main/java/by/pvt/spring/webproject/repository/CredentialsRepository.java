package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

}