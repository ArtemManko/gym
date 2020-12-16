package by.pvt.spring.webproject.controllers.forAdmin;


import by.pvt.spring.webproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UsersListController {

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/user")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "block/user/userList";
    }
}