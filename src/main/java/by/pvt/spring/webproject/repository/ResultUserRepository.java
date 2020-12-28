package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.ResultUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ResultUserRepository extends JpaRepository<ResultUser, Long> {

}