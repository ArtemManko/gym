package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Role;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
@ContextConfiguration(classes = UserRepositoryTest.EmbeddedPostgresContextConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserRepositoryTest {

    private static final String USERNAME = "bbb";
    private static final String EMAIL = "mankoartem2@gmail.com";
    private static final String CODE = "123";

    @Configuration
    @EntityScan(basePackageClasses = User.class)
    @EnableJpaRepositories(basePackageClasses = UserRepository.class)
    public static class EmbeddedPostgresContextConfiguration {

        @Bean
        @Primary
        public DataSource embeddedPG() throws IOException {
            return EmbeddedPostgres.start().getPostgresDatabase();
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername() {
        User findByUsername = userRepository.findByUsername(USERNAME);
        assertNotNull(findByUsername);
    }

    @Test
    public void findByEmail() {
        User findByEmail = userRepository.findByEmail(EMAIL);
        assertNotNull(findByEmail);
    }

    @Test
    public void findByActivationCode() {
        User findByActivationCode = userRepository.findByActivationCode(CODE);
        assertNotNull(findByActivationCode);
    }

    @Test
    public void findByRoles() {
        List<User> findByRoles = userRepository.findByRoles(Role.ADMIN);
        assertSame(1, findByRoles.size());
    }

    @Test
    public void findAll() {
        List<User> findAll = userRepository.findAll();
        assertSame(2, findAll.size());
    }
}