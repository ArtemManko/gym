package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "block/registration";
    }


    @PostMapping("/registration")
    public String addNewClient(User user, Map<String, Object> model) {

        if (!userService.addUser(user)) {
            model.put("message", "Username exists!");
            return "block/registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivate = userService.activateUser(code);

        if (isActivate) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "block/login";
    }
}
