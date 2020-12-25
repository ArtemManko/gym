package by.pvt.spring.webproject.controllers.forAdmin;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class UserEditController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        attributes(model, user);
        return "block/user/userEdit";
    }

    @PostMapping("/user-edit/{id}")
    public String editUser(User user, Model model) {

        if (!userService.checkEmail(user)) {
            attributes(model, user);
            model.addAttribute("errorUsername", "There is a user with this email or username");
            return "block/user/userEdit";
        }
        if (user.getLevels()==null||user.getRoles()==null){
            model.addAttribute("levels", Level.values());
            System.out.println("level");
            model.addAttribute("levelOrRoleError", "Level or Role not be null");
            return "block/user/userCreate";
        }


        if (!userService.checkPassword(user)) {
            attributes(model, user);
            model.addAttribute("errorPassword", "Different password!");
            return "block/user/userEdit";
        }
        userService.coderPassword(user);
        userService.saveUser(user);

        return "redirect:/user";
    }
    private void attributes(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("levels", Level.values());
    }
}
