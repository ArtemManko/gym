package by.pvt.spring.webproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = MyWebApplicationTest.class)
public class MyWebApplicationTest {

    @Test
    public void contextLoads() {
    }


}