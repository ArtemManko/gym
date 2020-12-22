package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class ScheduleService {

    static public final Logger LOGGER = Logger.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleRepository scheduleRepository;

    public boolean createSchedule(ScheduleWorkout scheduleWorkout) {

//     Set<ScheduleWorkout> scheduleFromDb = scheduleRepository.findByLevels(scheduleWorkout.getLevels());
//        System.out.println(scheduleFromDb.toString());
//        if (scheduleFromDb.getLevels().equals(scheduleWorkout.getLevels())) {
//            if (scheduleFromDb.getStart_time().equals(scheduleWorkout.getStart_time())) {
//                return false;
//            }
//        }

        if (scheduleWorkout.getLevels() == null || scheduleWorkout.getDays() == null ||
                scheduleWorkout.getStart_end_time() == null) {
            return false;
        }
        scheduleRepository.save(scheduleWorkout);
        return true;
    }

    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);

    }

    public ScheduleWorkout findById(Long id) {
        return scheduleRepository.getOne(id);
    }

    public void save(ScheduleWorkout scheduleWorkout) {
        scheduleRepository.save(scheduleWorkout);
    }

    public boolean editSchedule(ScheduleWorkout scheduleWorkout) {

//        List<ScheduleWorkout> levels = scheduleRepository.findByLevels(scheduleWorkout.getLevels());



        if (scheduleWorkout.getLevels() == null || scheduleWorkout.getDays() == null ||
                scheduleWorkout.getStart_end_time() == null) {
            return false;
        }
        scheduleRepository.save(scheduleWorkout);
        return true;
    }

}
