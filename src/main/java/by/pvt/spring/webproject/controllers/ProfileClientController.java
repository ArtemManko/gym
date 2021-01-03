package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class ProfileClientController {

    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/client/{id}")
    public String clientProfile(
            @PathVariable("id") Long id,
            Model model) {
        model.addAttribute("client", userService.findById(id));
        return "block/user/pages_client/clientProfile";
    }

    @GetMapping("/client-edit/{id}")
    public String editClientForm(
            @PathVariable("id") Long id,
            Model model) {

        User client = userService.findById(id);
        attributes(client, model);
        return "block/user/pages_client/clientEdit";
    }

    @PostMapping("/client-edit/{id}")
    public String editClient(@Valid User client, Model model) {

        if (!userService.checkPassword1(client, model)) {
            attributes(client, model);
            return "block/user/pages_client/clientEdit";
        }

        if (!userService.checkEmail(client, model)) {
            attributes(client, model);
            return "block/user/pages_client/clientEdit";
        }
        userService.addCredentialsUser(client);
        userService.coderPassword(client);
        userService.saveUser(client);

        return "redirect:/client/{id}";
    }

    private void attributes(User client, Model model) {
        model.addAttribute("client", client);
        model.addAttribute("levels", Level.values());
    }

    //    в сревис код проверки
    @GetMapping("/schedule-client/{id}")
    public String schedulesList(
            @PathVariable("id") Long id,
            Model model) {

        User client = userService.findById(id);
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
        scheduleService.deleteScheduleFromProfile(id_client, id_schedule);
        return "redirect:/schedule-client/{id}";
    }

}