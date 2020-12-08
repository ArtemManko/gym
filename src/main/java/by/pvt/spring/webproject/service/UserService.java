package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }


    //Add User
    public boolean addUser(User user) {

        User userFromDb = userRepository.findByUsername(user.getUsername());
        User emailFromDb = userRepository.findByEmail(user.getEmail());

        if (userFromDb != null || emailFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ADMIN));//change add role latter
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword2(passwordEncoder.encode(user.getPassword2()));
        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) { //isBlank ?
            String message = String.format(
                    "Hello,%s! \nWelcome to Team! Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    //Send Email and activate user after registration
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    public boolean saveUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        User emailFromDb = userRepository.findByEmail(user.getEmail());

        if (userFromDb != null || emailFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ADMIN));//change add role latter
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    public boolean editAndSaveUser(User user) {
        User editUser = userRepository.findById(user.getId()).get();

//        User userFromDb = userRepository.findByUsername(user.getUsername());
//        User emailFromDb = userRepository.findByEmail(user.getEmail());
//        if (userFromDb != null || emailFromDb != null) {
//            return false;
//        }
        editUser.setId(user.getId());
        editUser.setGender(user.getGender());
        editUser.setActive(true);
        editUser.setRoles(Collections.singleton(Role.ADMIN));//change add role latter
        editUser.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(editUser);
        userRepository.save(editUser);
        System.out.println(editUser);
        return true;
    }

    public boolean checkPassword(User user) {
        String passwordFromDb = userRepository.findByUsername(user.getUsername()).getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordInputExist = user.getPassword3();
        if (!encoder.matches(passwordInputExist, passwordFromDb) && passwordInputExist != null) {
            return false;
        }
        if (user.getPassword() != null && !user.getPassword().equals((user.getPassword2()))) {
            return false;
        }
        return true;
    }

}
