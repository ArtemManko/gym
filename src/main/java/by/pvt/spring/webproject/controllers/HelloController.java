package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user",user);
        return "block/hello";
    }
}