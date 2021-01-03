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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Controller
public class RegistrationUserController {
    static public final Logger LOGGER = Logger.getLogger(RegistrationUserController.class);
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
        return "block/registration";
    }

    @PostMapping("/registration")
    public String addNewClient(
            @RequestParam("g-recaptcha-response") String captcaResponce,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        if (!userService.recaptcha(bindingResult, model, captcaResponce, CAPTCHA_URL, restTemplate, secret)) {
            return "block/registration";
        }
        model.addAttribute("levels", Level.values());

        if (!userService.checkPassword2(user, model)) {
            return "block/registration";
        }
        if (!userService.levelNull(model, user)) {
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
