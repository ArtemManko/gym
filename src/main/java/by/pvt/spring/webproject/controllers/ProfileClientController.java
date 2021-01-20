package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
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

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH','ROLE_USER')")
public class ProfileClientController {

    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;

    //View user data for edit
    @GetMapping("/client/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public String clientProfile(
            @PathVariable("id") Long id,
            Model model) {
        User user = userService.findById(id);
        userService.membershipNotNull(user, model);
        model.addAttribute("client", userService.findById(id));
        return "block/user/pages_client/clientProfile";
    }

    //Edit user data
    @GetMapping("/client-edit/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public String editClientForm(
            @PathVariable("id") Long id,
            Model model) {
        User user = userService.findById(id);
        userService.membershipIdNotNull(user, model);
        attributes(user, model);
        return "block/user/pages_client/clientEdit";
    }

    @PostMapping("/client-edit/{id}")
    public String editClient(
            @Valid User user,
            BindingResult bindingResult,
            @RequestParam(name = "membership", required = false) Long id,
            Model model) {

        if (bindingResult.hasErrors()) {
            attributes(user, model);
            userService.membershipIdNotNull(user, model);
            return "block/user/pages_client/clientEdit";
        }
        if (!userService.checkPassword1(user, model)) {
            attributes(user, model);
            userService.membershipIdNotNull(user, model);
            return "block/user/pages_client/clientEdit";
        }
        if (!userService.checkEmail(user, model)) {
            attributes(user, model);
            userService.membershipIdNotNull(user, model);
            return "block/user/pages_client/clientEdit";
        }
        userService.membershipNotNull(user, model);
        model.addAttribute("client", user);
        if (user.getMembership() != null) {
            user.getMembership().setId(id);
        }
        userService.addCredentialsUser(user);
        return "redirect:/client/{id}";
    }

    //View schedule for Client
    @GetMapping("/schedule-client/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public String schedulesList(
            @PathVariable("id") Long id,
            Model model) {
        scheduleService.scheduleClient(id, model);
        return "block/user/pages_client/clientSchedule";
    }

    //Delete sung up
    @GetMapping("schedule-client-delete/{id}/{id_schedule}")
    @PreAuthorize("#id == authentication.principal.id")
    public String deleteClientSchedule(
            @PathVariable("id") Long id,
            @PathVariable("id_schedule") Long id_schedule
    ) {
        scheduleService.deleteScheduleFromProfile(id, id_schedule);
        return "redirect:/schedule-client/{id}";
    }

    private void attributes(User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("levels", Level.values());
    }
}