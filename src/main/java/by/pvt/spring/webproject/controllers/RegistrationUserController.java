package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.dto.CaptchaResponseDto;
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
import java.util.Collections;
import java.util.Map;

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

        String url = String.format(CAPTCHA_URL, secret, captcaResponce);
        CaptchaResponseDto response = restTemplate.postForObject(
                url, Collections.emptyList(), CaptchaResponseDto.class
        );

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");

        }
        model.addAttribute("levels", Level.values());
        if (user.getPassword() != null && !user.getPassword().equals((user.getPassword2()))) {

            model.addAttribute("passwordError", "Passwords are different!");
            return "block/registration";
        }
        if (bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.addAttribute("levels", Level.values());
            model.mergeAttributes(errors);
            return "block/registration";
        }
        if (user.getLevels() == null) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("levelError", "Level not be null");
            return "block/registration";
        }
        if (!userService.addUser(user)) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("usernameError", "Username or email exists!");
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
