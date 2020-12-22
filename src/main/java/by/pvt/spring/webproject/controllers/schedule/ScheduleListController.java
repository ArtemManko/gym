package by.pvt.spring.webproject.controllers.schedule;


import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ScheduleListController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/schedule-list")
    public String scheduleList(Model model) {
        model.addAttribute("schedules", scheduleRepository.findAll());
        return "block/schedule/scheduleList";
    }
}