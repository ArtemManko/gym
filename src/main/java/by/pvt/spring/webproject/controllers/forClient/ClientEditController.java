package by.pvt.spring.webproject.controllers.forClient;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.service.ClientService;
import by.pvt.spring.webproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")

public class ClientEditController {

    @Autowired
    private ClientService clientService;


    @GetMapping("/client-edit/{id}")
    public String editClientForm(@PathVariable("id") Long id, Model model) {
        User client = clientService.findById(id);
        model.addAttribute("client", client);
        model.addAttribute("roles", Role.values());
        return "block/user/pagesForClient/clientEdit";
    }

    @PostMapping("/client-edit/{id}")
    public String editClient(User client, Model model) {

        if (!clientService.checkPassword(client)) {
          model.addAttribute("errorPassword","Different password!");
            return "block/user/pagesForClient/clientEdit";
        }

//        if (!clientService.checkEmail(client)) {
//            model.addAttribute("errorUsername", "There is a user with this email or username");
//            return "block/user/pagesForClient/clientEdit";
//        }

        clientService.coderPassword(client);
        clientService.saveUser(client);

        return "redirect:/client/{id}";
    }
}
