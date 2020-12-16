package by.pvt.spring.webproject.controllers.forAdmin;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserEditController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/user-edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "block/user/userEdit";
    }

    @PostMapping("/user-edit/{id}")
    public String ceditUser(User user, Model model) {
        if (!userService.checkPassword(user)) {
            model.addAttribute("errorPassword", "Different password!");
            return "block/user/userEdit";
        }
        if (!userService.checkEmail(user)) {
            model.addAttribute("errorUsername", "There is a user with this email or username");
            return "block/user/userEdit";
        }

        userService.coderPassword(user);
        userService.saveUser(user);

        return "redirect:/user";
    }
}
