package by.pvt.spring.webproject.controllers.schedule;


import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleDeleteController {


    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("schedule-delete/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        scheduleService.deleteById(id);
        return "redirect:/schedule-list";
    }
}