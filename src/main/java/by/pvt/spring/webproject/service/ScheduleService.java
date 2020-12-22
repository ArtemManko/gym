package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class ScheduleService {

    static public final Logger LOGGER = Logger.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public boolean createSchedule(ScheduleWorkout scheduleWorkout) {
//проверить расписание на повторение
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
//проверка на повторение
//        List<ScheduleWorkout> levels = scheduleRepository.findByLevels(scheduleWorkout.getLevels());


        if (scheduleWorkout.getLevels() == null || scheduleWorkout.getDays() == null ||
                scheduleWorkout.getStart_end_time() == null) {
            return false;
        }
        scheduleRepository.save(scheduleWorkout);
        return true;
    }

    public boolean checkSingUpClient(Long id_user, Long id_schedule) {

        ScheduleWorkout scheduleWorkoutDB = findById(id_schedule);
        User userDB = userService.findById(id_user);
        for (ScheduleWorkout sc : userDB.getSchedule_workouts()) {
            if (sc.equals(scheduleWorkoutDB)) {
                System.out.println("false");
                return false;
            }
        }
        System.out.println("true");
        return true;
    }
}
