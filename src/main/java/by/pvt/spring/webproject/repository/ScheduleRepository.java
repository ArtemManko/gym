package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.enums.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ScheduleRepository extends JpaRepository<ScheduleWorkout, Long> {
    Set<ScheduleWorkout> findByLevelsIn(Set<Level> levels);

    Page<ScheduleWorkout> findAll(Pageable pageable);

}
