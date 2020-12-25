package by.pvt.spring.webproject.controllers.forClient;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.ScheduleRepository;
import by.pvt.spring.webproject.service.ClientService;
import by.pvt.spring.webproject.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class ClientScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/schedule-client/{id}")
    public String schedulesList(
            @PathVariable("id") Long id,
            Model model) {

        User client = clientService.findById(id);
        model.addAttribute("client", client);
        model.addAttribute("levels", Level.values());
        model.addAttribute("schedules", client.getSchedule_workouts());
        return "block/user/pages_client/clientSchedule";
    }

    @GetMapping("schedule-client-delete/{id}/{id_schedule}")
    public String deleteClientSchedule(
            @PathVariable("id") Long id_client,
            @PathVariable("id_schedule") Long id_schedule
    ) {

        ScheduleWorkout scheduleWorkout = scheduleService.findById(id_schedule);
        User client = clientService.findById(id_client);
        scheduleWorkout.getUsers().remove(client);
        client.getSchedule_workouts().remove(scheduleWorkout);
        clientService.saveUser(client);
        scheduleService.save(scheduleWorkout);

        return "redirect:/schedule-client/{id}";
    }


}