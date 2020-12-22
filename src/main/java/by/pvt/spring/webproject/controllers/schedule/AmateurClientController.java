package by.pvt.spring.webproject.controllers.schedule;

import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Day;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class AmateurClientController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/amateur/{id}")
    public String amateurGet(@AuthenticationPrincipal User user, ScheduleWorkout scheduleWorkout, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("amateur", "Amateur");
        List<ScheduleWorkout> scheduleWorkouts = scheduleRepository.findAll();
        Set<Level> levels = new HashSet<>();
        levels.add(Level.AMATEUR);
        List<ScheduleWorkout> scheduleWorkoutList = new ArrayList<>();
        for (ScheduleWorkout schedule : scheduleWorkouts) {
            if (schedule.getLevels().equals(levels)) {
                scheduleWorkoutList.add(schedule);
            }
            model.addAttribute("schedules", scheduleWorkoutList);
        }

        return "block/schedule/schedule";
    }

    @PostMapping("/amateur/{id}")
    public String beginnerPost(@Valid User user, Model model, ScheduleWorkout scheduleWorkout) {


        return "redirect:/hello";
    }
}