package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class HelloController {

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH','ROLE_USER')")
    @GetMapping("/hello")
    public String hello(
            @AuthenticationPrincipal User user,
            Model model) {
        model.addAttribute("user", user);
        model.addAttribute("levels", Level.values());

        return "block/hello";
    }

    @GetMapping("/")
    public String home(Principal principal, User user, Model model) {

        if (principal != null) {
            try {
                return "redirect:/hello";
            } catch (ClassCastException e) {
                return "redirect:/hello";
            }
        }
        model.addAttribute("user", user);
        return "block/hello";
    }


}



