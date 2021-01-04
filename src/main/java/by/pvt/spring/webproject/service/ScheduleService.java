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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    //Crate List Schedule By Level
    public Object level(Level level) {
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getLevels().equals(level))
                .collect(Collectors.toList());
    }

    //Check Schedule, if exist
    public boolean checkScheduleExist(ScheduleWorkout scheduleWorkout) {

        return scheduleRepository.findByLevelsIn(Collections.singleton(scheduleWorkout.getLevels())).stream()
                .anyMatch(sc->sc.equals(scheduleWorkout));
    }

    //Delete Schedule By Id
    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }

    //Sing Up Client and Save
    public void singUpClient(ScheduleWorkout scheduleWorkout, User user) {
        user.getSchedule_workouts().add(scheduleWorkout);
        userRepository.save(user);
    }

    //Find Schedule By ID
    public ScheduleWorkout findById(Long id) {
        return scheduleRepository.getOne(id);
    }

    public void save(ScheduleWorkout scheduleWorkout) {
        scheduleRepository.save(scheduleWorkout);
    }

    //When we edit schedule, check if exist
    public boolean editSchedule(ScheduleWorkout scheduleWorkout) {

        return scheduleRepository.findByLevelsIn(Collections.singleton(scheduleWorkout.getLevels())).stream()
                .filter(sc -> sc.equals(scheduleWorkout))
                .collect(Collectors.toSet())
                .stream().anyMatch(sc -> !sc.getId().equals(scheduleWorkout.getId()));
    }

    //Check Client Schedule,if exist
    public boolean checkSingUpClient(Long id_user, Long id_schedule) {

        return userService.findById(id_user).getSchedule_workouts().stream()
                .anyMatch(sc -> sc.equals(findById(id_schedule)));
    }

    //Check all value not be null
    public boolean createScheduleNull(ScheduleWorkout scheduleWorkout) {
        if (scheduleWorkout.getLevels() == null || scheduleWorkout.getDays() == null ||
                scheduleWorkout.getStart_end_time() == null) {
            return false;
        }

        return true;
    }

    //Cancel visit client workout
    public void deleteScheduleFromProfile(Long id_client, Long id_schedule) {
        ScheduleWorkout scheduleWorkout = findById(id_schedule);
        User client = userService.findById(id_client);
        scheduleWorkout.getUsers().remove(client);
        client.getSchedule_workouts().remove(scheduleWorkout);
        userService.saveUser(client);
        save(scheduleWorkout);
    }

    //Admin Delete Schedule and send email about cancel all Client
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

    //List Schedule for Coach
    public void listScheduleForCoach(Long id, Model model) {
        model.addAttribute("coach", userService.findById(id));
        model.addAttribute("schedules", userService.findById(id).getSchedule_workouts());
    }

    //Create Schedule for Coach
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
