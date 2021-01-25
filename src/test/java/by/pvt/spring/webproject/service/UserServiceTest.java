package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void coderPassword() {

        User user = new User();
        user.setId(10L);
        user.setFirst_name("Koly");
        user.setLast_name("New");
        user.setUsername("Koly");
        user.setPassword("1234");
        user.setEmail("Kol@gm.com");
        user.setCountry("Bel");
        user.setCity("Grodno");
        user.setStreet("new");

        userService.coderPassword(user);

    }
}