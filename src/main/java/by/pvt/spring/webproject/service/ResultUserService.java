package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.ResultUser;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.repository.ResultUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ResultUserService {

    static public final Logger LOGGER = Logger.getLogger(ResultUserService.class);

    @Autowired
    private ResultUserRepository resultUserRepository;
    @Autowired
    private UserService userService;


    public ResultUser findById(Long id) {
        return resultUserRepository.getOne(id);
    }

    public void addResult(ResultUser result, Long id) {
        User user = userService.findById(id);
        result.setUser(user);
        user.getResultUsers().add(result);
        userService.saveUser(user);
    }

    public void deleteResult(Long id_result, Long id_user) {

        ResultUser resultUser = findById(id_result);
        User user = userService.findById(id_user);
        user.getResultUsers().remove(resultUser);
        resultUserRepository.deleteById(id_result);

//        userService.saveUser(user);

    }
}
