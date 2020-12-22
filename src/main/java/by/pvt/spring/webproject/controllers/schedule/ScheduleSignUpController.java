package by.pvt.spring.webproject.controllers.schedule;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.repository.UserRepository;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleSignUpController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("schedule-singup/{id}/{id_schedule}")
    public String singUpSchedule(
            @PathVariable("id") Long id,
            @PathVariable("id_schedule") Long id_schedule,
            ScheduleWorkout schedule,
            User users
    ) {

        ScheduleWorkout scheduleWorkout = scheduleService.findById(id_schedule);
        User user = userService.findById(id);

        System.out.println("1");
        Set<Level> beginner = new HashSet<>();
        beginner.add(Level.BEGINNER);
        System.out.println("2");
        Set<Level> amateur = new HashSet<>();
        amateur.add(Level.AMATEUR);
        System.out.println("3");
        Set<Level> professional = new HashSet<>();
        professional.add(Level.PROFESSIONAL);
        System.out.println("4");

        if (scheduleWorkout.getLevels().equals(beginner)) {
           

            System.out.println("5");
//            System.out.println("Collection " + Collections.singletonList(scheduleWorkout).toString());
//            user.setSchedule_workouts(Collections.singletonList(scheduleWorkout));
//            System.out.println("User: " + user);
//            System.out.println("Schedule: " + scheduleWorkout);
//            userRepository.save(user);


            return "redirect:/beginner/{id}";
        }
        if (scheduleWorkout.getLevels().equals(amateur)) {

            return "redirect:/amateur/{id}";
        }

        if (scheduleWorkout.getLevels().equals(professional)) {

            return "redirect:/professional/{id}";

        }
        return "redirect:/hello";
    }
}
