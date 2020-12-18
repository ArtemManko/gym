package by.pvt.spring.webproject.controllers.forAdmin;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.UserRepository;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserCreateController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/user-create")
    public String createUserForm(User user, Model model) {
        model.addAttribute("levels", Level.values());
        return "block/user/userCreate";
    }

    @PostMapping("/user-create")
    public String createUser(@Valid User user, BindingResult bindingResult, Model model) {

        if (user.getPassword() != null && !user.getPassword().equals((user.getPassword2()))) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("message1", "Different password!");
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