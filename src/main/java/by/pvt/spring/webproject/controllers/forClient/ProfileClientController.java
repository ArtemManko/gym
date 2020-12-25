package by.pvt.spring.webproject.controllers.forClient;


import by.pvt.spring.webproject.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")
public class ProfileClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/client/{id}")
    public String clientProfile(
            @PathVariable("id") Long id,
            Model model) {
        model.addAttribute("client", clientService.findById(id));
        return "block/user/pages_client/clientProfile";
    }

}