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

    //Choose method recover password
    @GetMapping("/forgot-page")
    public String forgotPageGet(Model model) {
        return "block/forgotPassword/forgotPage";
    }

    //Use old password, from credentials, not active
    @GetMapping("/forgot-oldpassword")
    public String forgotOldPasswordGet(Model model) {
        return "block/forgotPassword/forgotOldPassword";
    }

    @PostMapping("/forgot-oldpassword")
    public String forgotOldPasswordPost(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        User userDB = userService.findByUsername(username);

        if (userService.notFoundUsername(userDB, model)) {
            return "block/forgotPassword/forgotOldPassword";
        }
        if (!userService.notFoundPassword(username, password, model, userDB)) {
            System.out.println("2");
            return "block/forgotPassword/forgotOldPassword";
        }
        model.addAttribute("user", userDB);
        return "block/forgotPassword/newPassword";
    }

    //Use email for recover password, send link with activate code
    @GetMapping("/forgot")
    public String forgotPasswordGet(Model model) {
        return "block/forgotPassword/forgotPassword";
    }

    @PostMapping("/forgot")
    public String forgotPasswordPost(
            @RequestParam String email,
            Model model) {
        if (!userService.forgotPassword(email, model)) {
            return "block/forgotPassword/forgotPassword";
        }
        return "redirect:/login";
    }

    //Input new password
    @GetMapping("/password/{code}")
    public String activate(Model model, @PathVariable String code) {
        userService.activateCodeForNewPassword(model, code);
        return "block/forgotPassword/newPassword";
    }

    @PostMapping("/password/{id}")
    public String newPassword(
            @PathVariable("id") Long id,
            @RequestParam String password,
            @RequestParam String password2,
            Model model
    ) {
        User user = userService.findById(id);
        if (!userService.checkPassword3(user, password, password2, model)) {
            return "block/forgotPassword/newPassword";
        }
        userService.addCredentialsUser(user);
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);
        return "redirect:/login";
    }
}