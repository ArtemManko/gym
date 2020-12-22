package by.pvt.spring.webproject.controllers.schedule;

import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Day;
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
public class BeginnerClientController {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/beginner/{id}")
    public String beginnerGet(@AuthenticationPrincipal User user, ScheduleWorkout scheduleWorkout, Model model) {

        model.addAttribute("user", user);
        model.addAttribute("beginner", "Beginner");

        List<ScheduleWorkout> scheduleWorkouts = scheduleRepository.findAll();
        Set<Level> levels = new HashSet<>();
        levels.add(Level.BEGINNER);
        List<ScheduleWorkout> scheduleWorkoutList = new ArrayList<>();
        for (ScheduleWorkout schedule : scheduleWorkouts) {
            if (schedule.getLevels().equals(levels)) {
                scheduleWorkoutList.add(schedule);
            }
            model.addAttribute("schedules", scheduleWorkoutList);
        }

        return "block/schedule/schedule";
    }
}