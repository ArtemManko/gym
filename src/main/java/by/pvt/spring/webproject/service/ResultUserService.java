package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.ResultUser;
import by.pvt.spring.webproject.repository.ResultUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ResultUserService  {

    static public final Logger LOGGER = Logger.getLogger(ResultUserService.class);

    @Autowired
    private ResultUserRepository resultUserRepository;
    @Autowired
    private ClientService clientService;



    public ResultUser findById(Long id) {
        return resultUserRepository.getOne(id);
    }

    public void addResult(ResultUser result, Long id) {
        User user = clientService.findById(id);
        result.setUser(user);
        user.getResultUsers().add(result);
        clientService.saveUser(user);
    }

    public void deleteResult(Long id_result, Long id_user) {

        ResultUser resultUser = findById(id_result);
        User user = clientService.findById(id_user);
        user.getResultUsers().remove(resultUser);
        resultUserRepository.deleteById(id_result);

//        clientService.saveUser(user);

    }
}
