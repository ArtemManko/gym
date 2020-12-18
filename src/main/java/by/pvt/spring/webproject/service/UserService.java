package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import javafx.concurrent.ScheduledService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    static public final Logger LOGGER = Logger.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User checkActivationCode = userRepository.findByUsername(username);
        if (checkActivationCode.getActivationCode() != null) {
            throw new UsernameNotFoundException(username);
        }
        return userRepository.findByUsername(username);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean createUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        User emailFromDb = userRepository.findByEmail(user.getEmail());

        if (userFromDb != null || emailFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.CLIENT));//change add role latter
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
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

    public void coderPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public boolean checkEmail(User user) {
        User usernameFromDb = userRepository.findByUsername(user.getUsername());
        User emailFromDb = userRepository.findByEmail(user.getEmail());

        if (user.getUsername() != null && user.getUsername().equals(usernameFromDb.getUsername())) {
            if (user.getId().equals(usernameFromDb.getId())) {
                System.out.println("true username");
            } else {
                return false;
            }
        }
        try {
            if (user.getEmail() != null && user.getEmail().equals(emailFromDb.getEmail())) {
                if (user.getId().equals(emailFromDb.getId())) {
                    System.out.println("true email");
                } else {
                    return false;
                }
            }

        } catch (NullPointerException e) {
            LOGGER.info("emailFromDb send null", e);
        }
        return true;
    }


}
