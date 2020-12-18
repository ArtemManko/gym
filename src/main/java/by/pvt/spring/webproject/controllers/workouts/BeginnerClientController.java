package by.pvt.spring.webproject.controllers.workouts;

import by.pvt.spring.webproject.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class BeginnerClientController {

    @GetMapping("/beginner/{id}")
    public String beginnerGet(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user",user);
        model.addAttribute("beginner","Beginner");
        return "block/schedule";
    }
}