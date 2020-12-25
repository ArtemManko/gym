package by.pvt.spring.webproject.controllers.schedule;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Transactional
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleDeleteController {


    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    UserService userService;

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
}