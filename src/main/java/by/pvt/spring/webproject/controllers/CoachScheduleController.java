package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','COACH')")
public class CoachScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * List schedule for coach
     */
    @GetMapping("/schedule-coach/{id}")
    public String schedulesList(@PathVariable("id") Long id, Model model) {
        scheduleService.listScheduleForCoach(id, model);
        return "block/user/coachSchedule";
    }

    /**
     * Delete schedule and send email for client
     */
    @GetMapping("schedule-coach-delete/{id}/{id_schedule}")
    public String deleteClientSchedule(
            @PathVariable("id_schedule") Long id_schedule,
            @PathVariable String id
    ) {
        scheduleService.deleteSchedule(id_schedule);
        return "redirect:/schedule-coach/{id}";
    }
}