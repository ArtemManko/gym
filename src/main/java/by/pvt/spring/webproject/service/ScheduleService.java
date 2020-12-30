package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.util.*;


@Service
@Transactional
public class ScheduleService {

    static public final Logger LOGGER = Logger.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public boolean checkScheduleExist(ScheduleWorkout scheduleWorkout) {

        Set<ScheduleWorkout> scheduleByLevelFromDb = scheduleRepository.findByLevelsIn
                (Collections.singleton(scheduleWorkout.getLevels()));
        for (ScheduleWorkout sc : scheduleByLevelFromDb) {
            if (sc.getLevels().equals(scheduleWorkout.getLevels())) {
                if (sc.getStart_end_time().equals(scheduleWorkout.getStart_end_time())) {
                    if (sc.getDays().equals(scheduleWorkout.getDays())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean createScheduleNull(ScheduleWorkout scheduleWorkout) {
        if (scheduleWorkout.getLevels() == null || scheduleWorkout.getDays() == null ||
                scheduleWorkout.getStart_end_time() == null) {
            return false;
        }

        return true;
    }

    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }

    public void singUpClient(ScheduleWorkout scheduleWorkout, User user) {
        user.getSchedule_workouts().add(scheduleWorkout);
        userRepository.save(user);
    }

    public ScheduleWorkout findById(Long id) {
        return scheduleRepository.getOne(id);
    }

    public void save(ScheduleWorkout scheduleWorkout) {
        scheduleRepository.save(scheduleWorkout);
    }

    public boolean editSchedule(ScheduleWorkout scheduleWorkout) {

        Set<ScheduleWorkout> scheduleByLevelFromDb = scheduleRepository.findByLevelsIn(
                Collections.singleton(scheduleWorkout.getLevels()));
        for (ScheduleWorkout sc : scheduleByLevelFromDb) {
            if (sc.getLevels().equals(scheduleWorkout.getLevels())) {
                if (sc.getStart_end_time().equals(scheduleWorkout.getStart_end_time())) {
                    if (sc.getDays().equals(scheduleWorkout.getDays())) {
                        if (!sc.getId().equals(scheduleWorkout.getId())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;

    }

    public boolean checkSingUpClient(Long id_user, Long id_schedule) {

        ScheduleWorkout scheduleWorkoutDB = findById(id_schedule);
        User userDB = userService.findById(id_user);
        for (ScheduleWorkout sc : userDB.getSchedule_workouts()) {
            if (sc.equals(scheduleWorkoutDB)) {
                return false;
            }
        }
        return true;
    }
}
