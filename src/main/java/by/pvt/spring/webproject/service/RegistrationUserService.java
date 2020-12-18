package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.RegistrationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class RegistrationUserService implements UserDetailsService {
    @Autowired
    private RegistrationUserRepository registrationUserRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return registrationUserRepository.findByUsername(username);
    }

    //Add User, registration
    public boolean addUser(User user) {

        User userFromDb = registrationUserRepository.findByUsername(user.getUsername());
        User emailFromDb = registrationUserRepository.findByEmail(user.getEmail());
        System.out.println(emailFromDb);
        if (userFromDb != null || emailFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.CLIENT));
//        user.setLevels(Collections.singleton(Level.BEGINNER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword2(passwordEncoder.encode(user.getPassword2()));
        registrationUserRepository.save(user);

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
        User user = registrationUserRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        registrationUserRepository.save(user);
        return true;
    }
}
