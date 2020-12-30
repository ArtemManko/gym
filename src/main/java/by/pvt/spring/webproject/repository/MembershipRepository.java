package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

}