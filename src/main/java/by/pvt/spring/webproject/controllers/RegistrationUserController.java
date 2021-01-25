package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class RegistrationUserController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration(Model model) {

        model.addAttribute("levels", Level.values());
        model.addAttribute("user", new User());
        return "block/registration";
    }

    @PostMapping("/registration")
    public String addNewClient(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("user", user);
            return "block/registration";
        }
        if (!userService.recaptcha(bindingResult, model, captchaResponse, CAPTCHA_URL, restTemplate, secret, user)) {
            return "block/registration";
        }
        model.addAttribute("levels", Level.values());

        if (!userService.checkPassword2(user, model)) {
            return "block/registration";
        }
        if (!userService.addUser(user, model)) {
            return "block/registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {

        boolean isActivate = userService.activate(code);

        if (isActivate) {
            model.addAttribute("messageSuccess", "User successfully activated");
        } else {
            model.addAttribute("messageDanger", "Activation code is not found!");
        }

        return "block/login";
    }

}
