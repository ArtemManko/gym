package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class HelloController {

    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user",user);
        return "block/hello";
    }
}