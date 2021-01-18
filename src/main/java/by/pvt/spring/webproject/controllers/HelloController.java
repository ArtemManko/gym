package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.repository.UserRepository;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;


@Controller
public class HelloController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

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
                OAuth2Authentication auth = (OAuth2Authentication) principal;
                Map<String, String> authDetails = (Map<String, String>) auth.getUserAuthentication().getDetails();
                if (userRepository.findByEmail(authDetails.get("email")) == null) {
                    userService.addUserGoogle(authDetails, user);
                    model.addAttribute("principalUser", "Are you registered!\n" +
                            "Username and Password has been sent to you by email");
                    return "redirect:/logout";
                }
            } catch (ClassCastException e) {
                return "redirect:/hello";
            }
        }
        model.addAttribute("user", user);
        return "block/hello";
    }


}



