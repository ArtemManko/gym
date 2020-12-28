package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class CoachService implements UserDetailsService {

    static public final Logger LOGGER = Logger.getLogger(CoachService.class);
    @Autowired
    private MailSender mailSender;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (userRepository.findByUsername(username).getActivationCode() != null) {
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

        if (userRepository.findByUsername(user.getUsername()) != null ||
                userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.CLIENT));//change add role latter
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean checkPassword(User user) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(user.getPassword3(), userRepository.findByUsername(user.getUsername()).getPassword())
                && user.getPassword3() != null) {
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


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
