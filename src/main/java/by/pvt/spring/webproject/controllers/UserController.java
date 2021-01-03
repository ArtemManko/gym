package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
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
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;

    //CREATE
    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("levels", Level.values());
        return "block/user/userCreate";
    }


    @PostMapping("/user-create")
    public String createUser(@Valid User user, Model model) {

        if (!userService.checkPassword2(user, model)) {
            return "block/user/userCreate";
        }

        if (!userService.levelNull(model, user)) {
            return "block/user/userCreate";
        }

        if (!userService.createUser(user, model)) {
            return "block/user/userCreate";
        }
        return "redirect:/user";
    }

    //DELETE
    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        User user = userService.findById(id);
        userService.deleteCoach(user);
        userService.deleteById(id);

        return "redirect:/user";
    }

    //List users
    @GetMapping("/user")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "block/user/userList";
    }

    //EDIT USER
    @GetMapping("/user-edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        attributes(model, user);
        return "block/user/userEdit";
    }

    @PostMapping("/user-edit/{id}")
    public String editUser(@Valid User user, Model model) {
//Check Email and Username, if have exist
        if (!userService.checkEmail(user, model)) {
            attributes(model, user);
            return "block/user/userEdit";
        }
 //Role and Level not be null
        if (userService.leveleAndRoleNull(model, user)) {
            return "block/user/userEdit";
        }

        userService.saveUser(user);
        return "redirect:/user";
    }

    private void attributes(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("levels", Level.values());
    }
}