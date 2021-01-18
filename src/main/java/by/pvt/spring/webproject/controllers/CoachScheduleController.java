package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH','ROLE_USER')")//change latter
public class CoachScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserService userService;

    //List schedule for coach
    @GetMapping("/schedule-coach/{id}")
    public String schedulesList(@PathVariable("id") Long id, Model model) {
        scheduleService.listScheduleForCoach(id, model);
        return "block/user/coachSchedule";
    }

    //Delete schedule and send email for client
    @GetMapping("schedule-coach-delete/{id}/{id_schedule}")
    public String deleteClientSchedule(
            @PathVariable("id_schedule") Long id
//            @PathVariable("id") Long id_coach
    ) {
        scheduleService.deleteSchedule(id);
        return "redirect:/schedule-coach/{id}";
    }
}