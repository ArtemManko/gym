package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping("/forgot")
    public String forgotPasswordGet(Model model) {
        return "block/forgotPassword";
    }

    @PostMapping("/forgot")
    public String forgotPasswordGet(
            @RequestParam String email,
            Model model) {
        if (!userService.forgotPassword(email)) {
            model.addAttribute("emailError", "No found email!");
            return "block/forgotPassword";
        }
        return "redirect:/login";
    }

    @GetMapping("/password/{code}")
    public String activate(Model model, @PathVariable String code) {
        User userDB = userService.findByActivationCode(code);
        boolean isActivate = userService.activatePassword(code);

        if (isActivate) {
            model.addAttribute("user", userDB);
            model.addAttribute("messageSuccess", "Input new password");
        } else {
            model.addAttribute("user", userDB);
            model.addAttribute("messageDanger", "Activation code is not found!");
        }
        return "block/newPassword";
    }

    @PostMapping("/password/{id}")
    public String newPassword(
            @PathVariable("id") Long id,
            @RequestParam String password,
            @RequestParam String password2,
            Model model
    ) {
        User user = userService.findById(id);

        if (!password.equals(password2)) {
            model.addAttribute("user", user);
            model.addAttribute("errorPassword", "Different password!");
            return "block/newPassword";
        }

        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);
        return "redirect:/login";
    }

}