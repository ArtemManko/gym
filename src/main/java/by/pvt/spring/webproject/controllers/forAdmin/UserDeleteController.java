package by.pvt.spring.webproject.controllers.forAdmin;


import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.service.ScheduleService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class UserDeleteController {


    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        Set<Role> role = new HashSet<>();
        role.add(Role.COACH);
        User user = userService.findById(id);
        if (user.getRoles().equals(role)) {
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
}