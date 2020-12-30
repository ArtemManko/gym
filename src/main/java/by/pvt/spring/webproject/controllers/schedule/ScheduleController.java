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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleController {

    @Autowired
    UserService userService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleService scheduleService;


    @GetMapping("/schedule-create")
    public String createScheduleGet(ScheduleWorkout scheduleWorkout, Model model) {
        List<String> timeStartEnd = timeWorkouts();
        model.addAttribute("levels", Level.values());
        model.addAttribute("coaches", userService.findByRoles(Role.COACH));
        model.addAttribute("time", timeStartEnd);
        model.addAttribute("days", Day.values());
        model.addAttribute("schedule", scheduleWorkout);

        return "block/schedule/scheduleCreate";
    }

    @PostMapping("/schedule-create")
    public String createSchedulePost(
            @Valid ScheduleWorkout scheduleWorkout,
            @RequestParam(required = false) Long id,
//            BindingResult bindingResult,
            Model model) {

        List<String> timeStartEnd = timeWorkouts();

        if (!scheduleService.createScheduleNull(scheduleWorkout) || id == null) {
            attributes(scheduleWorkout, model, timeStartEnd);
            model.addAttribute("errorValue", "Set the value!");
            return "block/schedule/scheduleCreate";
        }

        if (!scheduleService.checkScheduleExist(scheduleWorkout)) {
            attributes(scheduleWorkout, model, timeStartEnd);
            model.addAttribute("errorSchedule", "Schedule exist!");
            return "block/schedule/scheduleCreate";
        }
        User coach = userService.findById(id);
        coach.getSchedule_workouts().add(scheduleWorkout);
        userService.saveUser(coach);

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

    private void attributes(@Valid ScheduleWorkout scheduleWorkout, Model model, List<String> timeStartEnd) {
        model.addAttribute("levels", Level.values());
        model.addAttribute("coaches", userService.findByRoles(Role.COACH));
        model.addAttribute("time", timeStartEnd);
        model.addAttribute("days", Day.values());
        model.addAttribute("schedule", scheduleWorkout);
    }


    @GetMapping("schedule-delete/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        ScheduleWorkout scheduleWorkout = scheduleService.findById(id);
        scheduleWorkout.getUsers().forEach(user -> {
            user.getSchedule_workouts().remove(scheduleWorkout);
            userService.saveUser(user);
        });
        scheduleService.deleteById(id);
        return "redirect:/schedule-list";
    }

    @GetMapping("/schedule-list")
    public String scheduleList(Model model) {
        model.addAttribute("schedules", scheduleRepository.findAll());
        return "block/schedule/scheduleList";
    }
}