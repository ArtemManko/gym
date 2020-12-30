package by.pvt.spring.webproject.controllers.schedule;

import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class AmateurClientController {
    // в сревис код проверки
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/amateur/{id}")
    public String amateurGet(
            User user,
            Model model) {


        List<ScheduleWorkout> scheduleWorkoutList = new ArrayList<>();

        scheduleRepository.findAll().forEach(schedule -> {
            if (schedule.getLevels().equals(Level.AMATEUR)) {
                scheduleWorkoutList.add(schedule);
            }
        });
        model.addAttribute("user", user);
        model.addAttribute("schedules", scheduleWorkoutList);
        return "block/schedule/schedule";
    }
}