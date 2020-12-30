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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleSignUpController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("schedule-singup/{id}/{id_schedule}")
    public String singUpSchedule(
            @PathVariable("id") Long id,
            @PathVariable("id_schedule") Long id_schedule,
            ScheduleWorkout schedule,
            User users,
            Model model
    ) {

        ScheduleWorkout scheduleWorkout = scheduleService.findById(id_schedule);
        User user = userService.findById(id);

        if (scheduleWorkout.getLevels().equals(Level.BEGINNER)) {
            if (!scheduleService.checkSingUpClient(id, id_schedule)) {
                return "redirect:/beginner/{id}";
            }
            scheduleService.singUpClient(scheduleWorkout, user);
            return "redirect:/beginner/{id}";
        }

        if (scheduleWorkout.getLevels().equals(Level.AMATEUR)) {
            if (!scheduleService.checkSingUpClient(id, id_schedule)) {
                return "redirect:/amateur/{id}";
            }
            scheduleService.singUpClient(scheduleWorkout, user);
            return "redirect:/amateur/{id}";
        }

        if (scheduleWorkout.getLevels().equals(Level.PROFESSIONAL)) {
            if (!scheduleService.checkSingUpClient(id, id_schedule)) {
                return "redirect:/professional/{id}";
            }
            scheduleService.singUpClient(scheduleWorkout, user);
            return "redirect:/professional/{id}";

        }
        return "redirect:/hello";

    }
}
