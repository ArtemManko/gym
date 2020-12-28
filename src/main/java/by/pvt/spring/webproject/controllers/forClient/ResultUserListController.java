package by.pvt.spring.webproject.controllers.forClient;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.ResultUser;
import by.pvt.spring.webproject.repository.ResultUserRepository;
import by.pvt.spring.webproject.service.ClientService;
import by.pvt.spring.webproject.service.ResultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")
public class ResultUserListController {

    @Autowired
    private ResultUserRepository resultUserRepository;
    @Autowired
    private ResultUserService resultUserService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/result-user/{id}")
    public String resultUserListGet(
            @PathVariable("id") Long id,
            Model model) {

        User client = clientService.findById(id);
        model.addAttribute("client", client);
        model.addAttribute("results", client.getResultUsers());

        return "block/user/pages_client/userResultList";
    }

    @GetMapping("result-delete/{id_result}/{id}")
    public String deleteResult(
            @PathVariable("id_result") Long id_result,
            @PathVariable("id") Long id_user
    ) {
        resultUserService.deleteResult(id_result,id_user);
        return "redirect:/result-user/{id}";
    }

    @GetMapping("/result-create/{id}")
    public String createResult(
            @PathVariable("id") Long id,
            Model model) {
        model.addAttribute("client", clientService.findById(id));
        return "block/user/pages_client/userResultCreate";
    }

    @PostMapping("/result-create/{id}")
    public String createUser(
            @PathVariable("id") Long id,
            @Valid ResultUser result,
            Model model) {

        resultUserService.addResult(result, id);

        return "redirect:/result-user/{id}";
    }
}