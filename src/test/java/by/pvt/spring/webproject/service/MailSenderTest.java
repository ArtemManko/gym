package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.Membership;
import by.pvt.spring.webproject.repository.MembershipRepository;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.io.IOException;

import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {MailSenderTest.EmbeddedPostgresContextConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@PropertySource("classpath:application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MailSenderTest {

    private static final String emailTo = "mankoartem2@gmail.com";
    private static final String subject = "Test";
    private static final String message = "Check test";
    @Configuration
    @EntityScan(basePackageClasses = org.springframework.mail.MailSender.class)
    @EnableJpaRepositories(basePackageClasses = JavaMailSender.class)
    public static class EmbeddedPostgresContextConfiguration {

        @Bean
        @Primary
        public DataSource embeddedPG() throws IOException {
            return EmbeddedPostgres.start().getPostgresDatabase();
        }


        @Bean
        public JavaMailSender javaMailSender() {
            return mock(JavaMailSender.class);
        }

        @Bean
        public MailSender mailSender() {
            return new MailSender();
        }

    }
    @Autowired
    private MailSender service;

    @Test
    public void sendTest() {
        service.send(emailTo, subject, message);

    }
}