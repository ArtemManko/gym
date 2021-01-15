package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/")
    public String home( User user, Model model) {
        model.addAttribute("user", user);
        return "block/hello";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH','CLIENT_GOOGLE')")
    @GetMapping("/hello")
    public String hello(
            @AuthenticationPrincipal User user,
            Model model) {

        model.addAttribute("user", user);
        model.addAttribute("levels", Level.values());

        return "block/hello";
    }
}

//    @GetMapping("/google/login")
//    public String login(@RequestParam(value = "code") String code) {
//        return "Code =  " + code;
//    }
//}
