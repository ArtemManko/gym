package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Day;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
//@Transactional
public class ScheduleService {

    static public final Logger LOGGER = Logger.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MailSender mailSender;

    public Object level(Level level) {
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getLevels().equals(level))
                .collect(Collectors.toList());
    }


    public boolean checkScheduleExist(ScheduleWorkout scheduleWorkout) {

        Set<ScheduleWorkout> scheduleByLevelFromDb = scheduleRepository.findByLevelsIn
                (Collections.singleton(scheduleWorkout.getLevels()));

        for (ScheduleWorkout sc : scheduleByLevelFromDb) {
            if (sc.equals(scheduleWorkout)) {
                return false;
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


    public void deleteScheduleFromProfile(Long id_client, Long id_schedule) {
        ScheduleWorkout scheduleWorkout = findById(id_schedule);
        User client = userService.findById(id_client);
        scheduleWorkout.getUsers().remove(client);
        client.getSchedule_workouts().remove(scheduleWorkout);
        userService.saveUser(client);
        save(scheduleWorkout);
    }

    public void deleteSchedule(Long id) {
        ScheduleWorkout scheduleWorkout = findById(id);
        assert scheduleWorkout != null;
        scheduleWorkout.getUsers().stream()
                .filter(user -> user.getEmail() != null)
                .forEach(user -> {
                    if (!StringUtils.isEmpty(user.getEmail())) {
                        String message = String.format(
                                "Hello,%s! \nSorry, you workout cancel! Check you schedule!",
                                user.getFirst_name()
                        );
                        mailSender.send(user.getEmail(), "Cancel workout", message);
                    }
                });
        scheduleWorkout.getUsers().stream()
                .filter(Objects::nonNull)
                .forEach(user -> {
                    user.getSchedule_workouts().remove(scheduleWorkout);
                    userService.saveUser(user);
                });

        deleteById(id);
    }

    public void listScheduleForCoach(Long id, Model model) {
        User coach = userService.findById(id);
        List<ScheduleWorkout> scheduleWorkouts = coach.getSchedule_workouts();
        model.addAttribute("coach", coach);
        model.addAttribute("schedules", scheduleWorkouts);
    }

    public void addSchedule(Long id, ScheduleWorkout scheduleWorkout) {

        User user = userService.findById(id);
        user.getSchedule_workouts().add(scheduleWorkout);
        userService.saveUser(user);
    }

    public Object timeWorkouts() {
        List<String> timeStartEnd = new ArrayList<>();
        timeStartEnd.add("12:00-13:30");
        timeStartEnd.add("14:00-15:30");
        timeStartEnd.add("18:00-19:30");
        timeStartEnd.add("20:00-21:30");
        return timeStartEnd;
    }

    public void attributes(ScheduleWorkout scheduleWorkout, Model model) {
        model.addAttribute("levels", Level.values());
        model.addAttribute("coaches", userService.findByRoles(Role.COACH));
        model.addAttribute("time", timeWorkouts());
        model.addAttribute("days", Day.values());
        model.addAttribute("schedule", scheduleWorkout);
    }

    public void scheduleClient(Long id, Model model) {
        User client = userService.findById(id);
        model.addAttribute("client", client);
        model.addAttribute("levels", Level.values());
        model.addAttribute("schedules", client.getSchedule_workouts());
    }
}
