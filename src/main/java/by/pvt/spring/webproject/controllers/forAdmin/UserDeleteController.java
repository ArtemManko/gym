package by.pvt.spring.webproject.controllers.forAdmin;


import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserDeleteController {


    @Autowired
    private UserService userService;

    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/user";
    }
}