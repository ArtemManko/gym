package by.pvt.spring.webproject.controllers.forAdmin;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class UserCreateController {

    @Autowired
    private UserService userService;


    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("levels", Level.values());
        return "block/user/userCreate";
    }

    @PostMapping("/user-create")
    public String createUser(@Valid User user, Model model) {

        if (user.getPassword() != null && !user.getPassword().equals((user.getPassword2()))) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("errorPassword", "Different password!");
            return "block/user/userCreate";
        }

        if (user.getLevels() == null) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("levelError", "Level not be null");
            return "block/user/userCreate";
        }
        if (!userService.createUser(user)) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("message2", "Username or email exists!");
            return "block/user/userCreate";
        }
        return "redirect:/user";
    }
}