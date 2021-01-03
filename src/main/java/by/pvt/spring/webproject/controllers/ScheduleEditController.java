package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleEditController {

    @Autowired
    UserService userService;
    @Autowired
    private ScheduleService scheduleService;
//Schedule EDIT
    @GetMapping("/schedule-edit/{id}")
    public String editScheduleForm(
            @PathVariable("id") Long id,
            Model model) {
        ScheduleWorkout scheduleWorkout = scheduleService.findById(id);
        scheduleService.attributes(scheduleWorkout, model);
        return "block/schedule/scheduleEdit";
    }

    @PostMapping("/schedule-edit/{id}")
    public String editUser(ScheduleWorkout scheduleWorkout, Model model) {
        if (!scheduleService.createScheduleNull(scheduleWorkout)) {
            scheduleService.attributes(scheduleWorkout, model);
            model.addAttribute("errorValue", "Set the value!");
            return "block/schedule/scheduleEdit";
        }
        if (!scheduleService.editSchedule(scheduleWorkout)) {
            scheduleService.attributes(scheduleWorkout, model);
            model.addAttribute("errorSchedule", "Schedule exist!");
            return "block/schedule/scheduleEdit";
        }
        scheduleService.save(scheduleWorkout);
        return "redirect:/schedule-list";
    }
}
