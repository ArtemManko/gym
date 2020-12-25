package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.enums.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
public interface ScheduleRepository extends JpaRepository<ScheduleWorkout, Long> {
    Set<ScheduleWorkout> findByLevelsIn(Set<Level> levels);



}
