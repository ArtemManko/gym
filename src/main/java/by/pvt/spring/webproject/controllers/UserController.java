package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.repository.UserRepository;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "block/user/userList";
    }

    @GetMapping("/user-create")
    public String createUserForm(User user) {
        return "block/user/userCreate";
    }

    @PostMapping("/user-create")
    public String createUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (!userService.saveUser(user)) {
            model.addAttribute("message", "Username or email exists!");
            return "block/user/userCreate";
        }
        return "redirect:/users";
    }

    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/user-edit/{id}")
    public String editUserForm(@PathVariable User id, Model model) {
        model.addAttribute("user", id);
        return "block/user/userEdit";
    }

    @PostMapping("/user-edit")

    public String editUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (!userService.checkPassword(user)) {
            model.addAttribute("passwordError", "Passwords are different!");
            return "block/user/userEdit";
        }

        if (!userService.editAndSaveUser(user)) {
            model.addAttribute("usernameError", "Username or email exists!");
            return "block/user/userEdit";
        }

        return "redirect:/users";
    }
}