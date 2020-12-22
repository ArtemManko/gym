package by.pvt.spring.webproject.controllers.schedule;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Day;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleEditController {


    @Autowired
    private ScheduleService scheduleService;


    @GetMapping("/schedule-edit/{id}")
    public String editScheduleForm(@PathVariable("id") Long id, Model model) {
        ScheduleWorkout scheduleWorkout = scheduleService.findById(id);
//        model.addAttribute("user", user);
        List<String> timeStartEnd = new ArrayList<>();
        timeStartEnd.add("12:00-13:30");
        timeStartEnd.add("14:00-15:30");
        timeStartEnd.add("18:00-19:30");
        timeStartEnd.add("20:00-21:30");
        model.addAttribute("levels", Level.values());
        model.addAttribute("time", timeStartEnd);
        model.addAttribute("days", Day.values());
        model.addAttribute("schedule", scheduleWorkout);
        return "block/schedule/scheduleEdit";
    }

    @PostMapping("/schedule-edit/{id}")
    public String editUser(ScheduleWorkout scheduleWorkout, Model model) {

        if (!scheduleService.editSchedule(scheduleWorkout)) {
            List<String> timeStartEnd = new ArrayList<>();
            timeStartEnd.add("12:00-13:30");
            timeStartEnd.add("14:00-15:30");
            timeStartEnd.add("18:00-19:30");
            timeStartEnd.add("20:00-21:30");
            model.addAttribute("levels", Level.values());
            model.addAttribute("time", timeStartEnd);
            model.addAttribute("days", Day.values());
            model.addAttribute("schedule", scheduleWorkout);
            model.addAttribute("errorSchedule", "Set the value!");
            return "block/schedule/scheduleEdit";
        }

        return "redirect:/schedule-list";
    }
}
