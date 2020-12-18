package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.ClientRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class ClientService implements UserDetailsService {

    static public final Logger LOGGER = Logger.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User checkActivationCode = clientRepository.findByUsername(username);
        if (checkActivationCode.getActivationCode() != null) {
            throw new UsernameNotFoundException(username);
        }
        return clientRepository.findByUsername(username);
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public User findById(Long id) {
        return clientRepository.getOne(id);
    }

    public User saveUser(User user) {
        return clientRepository.save(user);
    }

    public User findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    public boolean createUser(User user) {
        User userFromDb = clientRepository.findByUsername(user.getUsername());
        User emailFromDb = clientRepository.findByEmail(user.getEmail());

        if (userFromDb != null || emailFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.CLIENT));//change add role latter
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        clientRepository.save(user);
        return true;
    }

    public boolean checkPassword(User client) {
        try {

            String passwordFromDb = clientRepository.findByUsername(client.getUsername()).getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordInputExist = client.getPassword3();

            if (!encoder.matches(passwordInputExist, passwordFromDb) && passwordInputExist != null) {
                System.out.println("f");
                return false;
            }
            if (client.getPassword() != null && !client.getPassword().equals((client.getPassword2()))) {
                System.out.println("fa");
                return false;
            }
        } catch (NullPointerException e) {
            LOGGER.error("Null in ClientService checkPassword");
        }
        System.out.println("true");
        return true;
    }

    public void coderPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public boolean checkEmail(User user) {
        User usernameFromDb = clientRepository.findByUsername(user.getUsername());
        User emailFromDb = clientRepository.findByEmail(user.getEmail());
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
