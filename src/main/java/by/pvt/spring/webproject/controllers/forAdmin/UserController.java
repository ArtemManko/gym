package by.pvt.spring.webproject.controllers.forAdmin;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;

//CREATE
    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("levels", Level.values());
        return "block/user/userCreate";
    }

    //уменьшить код
    @PostMapping("/user-create")
    public String createUser(@Valid User user, Model model) {

        if (user.getPassword() != null && !user.getPassword().equals((user.getPassword2()))) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("errorPassword", "Different password!");
            return "block/user/userCreate";
        }

        if (user.getLevels() == null) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("levelError", "Level not be null");
            return "block/user/userCreate";
        }
        if (!userService.createUser(user)) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("message2", "Username or email exists!");
            return "block/user/userCreate";
        }
        return "redirect:/user";
    }

    //DELETE
    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        //перенсти в сервис проверку
        User user = userService.findById(id);
        if (user.getRoles().equals(Role.COACH)) {
            List<ScheduleWorkout> scheduleWorkouts = user.getSchedule_workouts();
            assert scheduleWorkouts != null;
            for (ScheduleWorkout schedule : scheduleWorkouts) {
                List<User> users = schedule.getUsers();
                userService.deleteById(id);
                if (users != null) {
                    for (User u : users) {
                        u.getSchedule_workouts().remove(schedule);
                        userService.saveUser(u);
                    }
                }
                scheduleService.deleteById(schedule.getId());
            }
            return "redirect:/user";
        }
        userService.deleteById(id);
        return "redirect:/user";
    }

    //List users
    @GetMapping("/user")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "block/user/userList";
    }

    //EDIT USER
    @GetMapping("/user-edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        attributes(model, user);
        return "block/user/userEdit";
    }

    @PostMapping("/user-edit/{id}")
    public String editUser(@Valid User user, Model model) {

        if (!userService.checkEmail(user, model)) {
            attributes(model, user);
            return "block/user/userEdit";
        }

        if (user.getLevels() == null || user.getRoles() == null) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("levelOrRoleError", "Level or Role not be null");
            return "block/user/userCreate";
        }


//        if (!userService.checkPassword(user)) {
//            attributes(model, user);
//            model.addAttribute("errorPassword", "Different password!");
//            return "block/user/userEdit";
//        }
//        userService.coderPassword(user);
        userService.saveUser(user);

        return "redirect:/user";
    }

    private void attributes(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("levels", Level.values());
    }
}