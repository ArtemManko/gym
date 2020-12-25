package by.pvt.spring.webproject.controllers.schedule;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Day;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleEditController {

    @Autowired
    UserService userService;
    @Autowired
    private ScheduleService scheduleService;


    @GetMapping("/schedule-edit/{id}")
    public String editScheduleForm(
            @PathVariable("id") Long id,
            Model model) {
        ScheduleWorkout scheduleWorkout = scheduleService.findById(id);
        List<String> timeStartEnd = timeWorkouts();
        attributes(scheduleWorkout, model, timeStartEnd);
        return "block/schedule/scheduleEdit";
    }

    @PostMapping("/schedule-edit/{id}")
    public String editUser(
            @RequestParam Long id_coach,
            ScheduleWorkout scheduleWorkout,
            Model model
    ) {
        List<String> timeStartEnd = timeWorkouts();

        if (!scheduleService.createScheduleNull(scheduleWorkout)) {
            attributes(scheduleWorkout, model, timeStartEnd);
            model.addAttribute("errorValue", "Set the value!");
            return "block/schedule/scheduleCreate";
        }

        if (!scheduleService.editSchedule(scheduleWorkout)) {
            attributes(scheduleWorkout, model, timeStartEnd);
            model.addAttribute("errorSchedule", "Schedule exist!");
            return "block/schedule/scheduleCreate";
        }
        User coach = userService.findById(id_coach);
        coach.getSchedule_workouts().add(scheduleWorkout);
        userService.saveUser(coach);
//        scheduleService.save(scheduleWorkout);
        return "redirect:/schedule-list";
    }

    private List<String> timeWorkouts() {
        List<String> timeStartEnd = new ArrayList<>();
        timeStartEnd.add("12:00-13:30");
        timeStartEnd.add("14:00-15:30");
        timeStartEnd.add("18:00-19:30");
        timeStartEnd.add("20:00-21:30");
        return timeStartEnd;
    }

    private void attributes(ScheduleWorkout scheduleWorkout, Model model, List<String> timeStartEnd) {
        model.addAttribute("coaches", userService.findByRoles(Role.COACH));
        model.addAttribute("time", timeStartEnd);
        model.addAttribute("levels", Level.values());
        model.addAttribute("days", Day.values());
        model.addAttribute("schedule", scheduleWorkout);
    }
}
