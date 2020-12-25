package by.pvt.spring.webproject.controllers.forCoach;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.service.CoachService;
import by.pvt.spring.webproject.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class CoachScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private CoachService coachService;

    @GetMapping("/schedule-coach/{id}")
    public String schedulesList(@PathVariable("id") Long id, Model model) {
        User coach = coachService.findById(id);
        List<ScheduleWorkout> scheduleWorkouts = coach.getSchedule_workouts();

        model.addAttribute("coach", coach);
        model.addAttribute("schedules", scheduleWorkouts);
        return "block/user/coachSchedule";
    }

    @GetMapping("schedule-coach-delete/{id}/{id_schedule}")
    public String deleteClientSchedule(
            @PathVariable("id_schedule") Long id,
            @PathVariable("id") Long id_coach
    ) {
        ScheduleWorkout scheduleWorkout = scheduleService.findById(id);
        assert scheduleWorkout != null;
        List<User> users = scheduleWorkout.getUsers();
        if (users != null) {
            for (User user : users) {
                user.getSchedule_workouts().remove(scheduleWorkout);
                coachService.saveUser(user);
            }
        }
        scheduleService.deleteById(id);
        return "redirect:/schedule-coach/{id}";
    }


}