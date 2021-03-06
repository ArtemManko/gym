package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.controllers.ControllerUtils;
import by.pvt.spring.webproject.entities.Credentials;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.dto.CaptchaResponseDto;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MembershipService membershipService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user.getActivationCode() != null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    /**
     * DELETE
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * FIND BY ID
     */
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    /**
     * SAVE USER
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * FIND ACTIVATE CODE
     */
    public User findByActivationCode(String code) {
        return userRepository.findByActivationCode(code);
    }

    /**
     * FIND BY USERNAME
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * FIND BY ROLE
     */
    public List<User> findByRoles(Role coach) {
        return userRepository.findByRoles(coach);
    }

    /**
     * CODER PASSWORD
     */
    public void coderPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    /**
     * CHECK CREDENTIALS
     */
    public boolean checkCredentialsPassword(String username, String password) {

        User userDB = findByUsername(username);
        return userDB.getCredentials().stream()
                .anyMatch(credentials -> new BCryptPasswordEncoder().matches(password, credentials.getPassword()));
    }

    /**
     * DELETE COACH AND SCHEDULE
     */
    @Transactional
    public void delete(User user) {
        try {
            if (user.getRoles().equals(Role.COACH) && user.getSchedule_workouts() != null) {
                user.getSchedule_workouts().forEach(scheduleWorkout -> {
                    scheduleWorkout.getUsers().forEach(user1 -> {
                        user1.getSchedule_workouts().remove(scheduleWorkout);
                        userRepository.save(user1);
                    });
                    scheduleService.deleteById(scheduleWorkout.getId());
                });
            }
        } catch (ConcurrentModificationException ignored) {
            log.error("ConcurrentModificationException " + ignored);
        }
    }

    /**
     * If User forgot password and use Send Email method
     */
    @Transactional
    public boolean forgotPassword(String email, Model model) {
        User emailFromDb = userRepository.findByEmail(email);

        if (emailFromDb == null) {
            model.addAttribute("emailError", "No found email!");
            return false;
        }

        emailFromDb.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(emailFromDb);

        if (!StringUtils.isEmpty(email)) {
            String message = String.format(
                    "Hello,%s! \nYou forgot password! Please, visit next link: http://localhost:8080/password/%s",
                    emailFromDb.getUsername(),
                    emailFromDb.getActivationCode()
            );
            mailSender.send(email, "Activation code", message);
        }
        return true;
    }

    /**
     * CHECK EMAIL AND USERNAME
     */
    public boolean checkEmail(User user, Model model) {
        try {
            User usernameDB = userRepository.findByUsername(user.getUsername());
            User emailDB = userRepository.findByEmail(user.getEmail());
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
            log.error("NullPointerException" + e);
        }
        return true;
    }

    /**
     * ACTIVATE USER (EMAIL SENDER)
     */
    public boolean activate(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }

    /**
     * CREATE USER
     */
    @Transactional
    public boolean createUser(User user, Model model) {

        if (userRepository.findByUsername(user.getUsername()) != null ||
                userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("user", user);
            model.addAttribute("message2", "Username or email exists!");
            return false;
        }
        user.setActive(true);
        user.setRoles(Role.CLIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    /**
     * ADD CREDENTIALS USER
     */
    public void addCredentialsUser(User user) {
        Credentials credentials = new Credentials();
        credentials.setPassword(user.getPassword());
        credentials.setActive(false);
        credentials.setCreateDate(new Date());
        credentials.setUser(user);
        user.getCredentials().add(credentials);
        coderPassword(user);
        saveUser(user);

    }

    /**
     * ADD USER IN DATA BASE, SEND EMAIL
     */
    @Transactional
    public boolean addUser(User user, Model model) {
        User userDB = userRepository.findByUsername(user.getUsername());
        User emailDB = userRepository.findByEmail(user.getEmail());
        if (userDB != null || emailDB != null) {
            model.addAttribute("user", user);
            model.addAttribute("levels", Level.values());
            model.addAttribute("usernameError", "Username or email exists!");
            return false;
        }
        user.setActive(true);
        user.setRoles(Role.CLIENT);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello,%s! \nWelcome to Team! Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
            System.out.println(message);
        }
        return true;
    }

    public boolean levelAndRoleNull(Model model, User user) {

        if (user.getLevels() == null || user.getRoles() == null) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("roles", Role.values());
            model.addAttribute("levelOrRoleError", "Level or Role not be null");
            return true;
        }
        return false;
    }

    /**
     * Check username if user forgot password
     */
    public boolean notFoundUsername(User userDB, Model model) {
        if (userDB == null) {
            model.addAttribute("user", userDB);
            model.addAttribute("usernameError", "Not found username!");
            return true;
        }
        return false;
    }

    /**
     * Check password in Data Base if user forgot password
     */
    public boolean notFoundPassword(String username, String password, Model model, User userDB) {
        if (checkCredentialsPassword(username, password)) {
            return true;
        }
        model.addAttribute("user", userDB);
        model.addAttribute("passwordError", "No found password!");
        return false;
    }

    /**
     * INPUT LEVEL, LEVEL NOT BE NULL
     */
    public boolean levelNull(Model model, User user) {
        if (user.getLevels() == null) {
            model.addAttribute("user", user);
            model.addAttribute("levels", Level.values());
            model.addAttribute("levelError", "Level not be null");
            return false;
        }
        return true;
    }

    /**
     * Check code, after send email user
     */
    public void activateCodeForNewPassword(Model model, String code) {
        User userDB = findByActivationCode(code);
        boolean isActivate = activate(code);

        if (isActivate) {
            model.addAttribute("user", userDB);
            model.addAttribute("messageSuccess", "Input new password");
        } else {
            model.addAttribute("user", userDB);
            model.addAttribute("messageDanger", "Activation code is not found!");
        }
    }

    /**
     * CHECK PASSWORD
     */
    public boolean checkPassword1(User user, Model model) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(user.getPassword3(), findById(user.getId()).getPassword())
                && user.getPassword3() != null || user.getPassword() != null
                && !user.getPassword().equals((user.getPassword2()))) {
            model.addAttribute("errorPassword", "Different password!");
            return false;
        }
        return true;
    }

    public boolean checkPassword2(User user, Model model) {

        if (user.getPassword() != null && !user.getPassword().equals((user.getPassword2()))) {
            model.addAttribute("levels", Level.values());
            model.addAttribute("user", user);
            model.addAttribute("errorPassword", "Different password!");
            return false;
        }
        return true;
    }

    public boolean checkPassword3(User user, String password, String password2, Model model) {
        if (password.equals(password2)) {
            return true;
        }
        model.addAttribute("user", user);
        model.addAttribute("errorPassword", "Different password!");
        return false;
    }

    public boolean recaptcha(BindingResult bindingResult, Model model, String captcaResponce, String CAPTCHA_URL,
                             RestTemplate restTemplate, String secret, User user) {
        String url = String.format(CAPTCHA_URL, secret, captcaResponce);
        CaptchaResponseDto response = restTemplate.postForObject(
                url, Collections.emptyList(), CaptchaResponseDto.class
        );
        if (!response.isSuccess() && bindingResult.hasErrors() || !response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.addAttribute("levels", Level.values());
            model.addAttribute("user", user);
            model.mergeAttributes(errors);
            return false;
        }
        return true;
    }

    public void membershipNotNull(User client, Model model) {
        if (client.getMembership() != null) {
            model.addAttribute("membership", client.getMembership());
            model.addAttribute("days", membershipService.membershipClient(client.getMembership()));
        }
    }

    public void membershipIdNotNull(User user, Model model) {
        if (user.getMembership() != null) {
            model.addAttribute("membership", user.getMembership().getId());
        }
    }

}


