package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ScheduleRepository extends JpaRepository<ScheduleWorkout, Long> {



//  List<ScheduleWorkout> findByLevels(Set<Level> levels);


}
