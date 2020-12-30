package by.pvt.spring.webproject.service;


import by.pvt.spring.webproject.entities.Credentials;
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
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class UserService implements UserDetailsService {

    static public final Logger LOGGER = Logger.getLogger(UserService.class);
    @Autowired
    private MailSender mailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user.getActivationCode() != null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
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
        user.setRoles(Role.CLIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean checkPassword(User user, Model model) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(user.getPassword3(), userRepository.findByUsername(user.getUsername()).getPassword())
                && user.getPassword3() != null ||
                user.getPassword() != null && !user.getPassword().equals((user.getPassword2()))) {
            model.addAttribute("errorPassword", "Different password!");
            return false;
        }
        return true;
    }

    public void coderPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public boolean checkEmail(User user, Model model) {
        try {
            User usernameDB = userRepository.findByUsername(user.getUsername());
            User emailDB = userRepository.findByEmail(user.getEmail());
            System.out.println(emailDB.getEmail());
            if (user.getUsername() != null && user.getUsername().equals(usernameDB.getUsername()) &&
                    user.getEmail() != null && user.getEmail().equals(emailDB.getEmail())) {
                if (user.getId().equals(usernameDB.getId()) && user.getId().equals(emailDB.getId())
                ) {

                } else {
                    model.addAttribute("errorUsername", "There is a user with this email or username");
                    return false;
                }
            }
        } catch (NullPointerException e) {
            LOGGER.info("emailFromDb send null", e);
        }
        return true;
    }

    public boolean forgotPassword(String email) {
        User emailFromDb = userRepository.findByEmail(email);

        if (emailFromDb == null) {
            return false;
        }

        emailFromDb.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(emailFromDb);

        if (!StringUtils.isEmpty(email)) { //isBlank ?
            String message = String.format(
                    "Hello,%s! \nYou forgot password! Please, visit next link: http://localhost:8080/password/%s",
                    emailFromDb.getUsername(),
                    emailFromDb.getActivationCode()
            );
            mailSender.send(email, "Activation code", message);
        }
        return true;
    }

    public boolean activate(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public User findByActivationCode(String code) {
        return userRepository.findByActivationCode(code);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findByRoles(Role coach) {
        return userRepository.findByRoles(coach);
    }

    public boolean checkCredentialsPassword(String username, String password) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User userDB = findByUsername(username);
        List<Credentials> credentialsList = userDB.getCredentials();

        for (Credentials credentials : credentialsList) {

            if (encoder.matches(password, credentials.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public void addCredentialsUser(User user) {
        Credentials credentials = new Credentials();
        credentials.setPassword(user.getPassword());
        credentials.setActive(false);
        credentials.setCreateDate(new Date());
        credentials.setUser(user);
        user.getCredentials().add(credentials);
    }

    public boolean addUser(User user) {
        User userDB = userRepository.findByUsername(user.getUsername());
        User emailDB = userRepository.findByEmail(user.getEmail());
        if (userDB != null || emailDB != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Role.CLIENT);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword2(passwordEncoder.encode(user.getPassword2()));
        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello,%s! \nWelcome to Team! Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }


}
