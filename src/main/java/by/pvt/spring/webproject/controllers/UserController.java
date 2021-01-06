package by.pvt.spring.webproject.controllers;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.stream.IntStream;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    //CREATE
    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("levels", Level.values());
        model.addAttribute("user", new User());
        return "block/user/userCreate";
    }


    @PostMapping("/user-create")
    public String createUser(
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("user", user);
            return "block/user/userCreate";
        }

        if (!userService.checkPassword2(user, model)) {
            return "block/user/userCreate";
        }

        if (!userService.createUser(user, model)) {
            return "block/user/userCreate";
        }
        return "redirect:/user";
    }

    //DELETE
    @GetMapping("user-delete/{id}")
    public String deleteUser(
            @PathVariable("id") Long id
    ) {

        User user = userService.findById(id);
        userService.deleteCoach(user);
        userService.deleteById(id);

        return "redirect:/user";
    }

    //List users
    @GetMapping("/user")
    public String userList(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page
    ) {
        Page<User> userPage = userRepository.findAll(
                PageRequest.of(page, 10, Sort.Direction.DESC, "id"));
        model.addAttribute("users", userPage);
        model.addAttribute("numbers", IntStream.range(0, userPage.getTotalPages()).toArray());
        return "block/user/userList";
    }

    //EDIT USER
    @GetMapping("/user-edit/{id}")
    public String editUserForm(
            @PathVariable("id") Long id,
            Model model
    ) {
        User user = userService.findById(id);
        userService.membershipIdNotNull(user, model);
        attributes(model, user);
        return "block/user/userEdit";
    }

    @PostMapping("/user-edit/{id}")
    public String editUser(
            @Valid User user,
            BindingResult bindingResult,
            @RequestParam(name = "membership", required = false) Long id,
            Model model) {

        if (bindingResult.hasErrors()) {
            userService.membershipIdNotNull(user, model);//CHECK???
            attributes(model, user);
            return "block/user/userEdit";
        }

        //Check Email and Username, if have exist
        if (!userService.checkEmail(user, model)) {
            userService.membershipIdNotNull(user, model);//CHECK???
            attributes(model, user);
            return "block/user/userEdit";
        }
        //Role and Level not be null
        if (userService.levelAndRoleNull(model, user)) {
            userService.membershipIdNotNull(user, model);
            attributes(model, user);
            return "block/user/userEdit";
        }
        if (user.getMembership() != null) {
            user.getMembership().setId(id);
        }
        userService.saveUser(user);
        return "redirect:/user";
    }

    private void attributes(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("levels", Level.values());
    }
}