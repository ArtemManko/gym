package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("isAnonymous()")
public class FrontController {

    @GetMapping("/")
    public String home(User user, Model model) {
        model.addAttribute("user", user);
        return "block/front";
    }
}
