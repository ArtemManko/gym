package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.Membership;
import by.pvt.spring.webproject.repository.MembershipRepository;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {MembershipServiceTest.EmbeddedPostgresContextConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@PropertySource("classpath:application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MembershipServiceTest {

    private static final Long ID = 1L;
    private static final Integer PRICE = 25;
    private static final String PAYMENT_ID_OLD = "PAYID-MAD7XRQ30R08908KS341031T";
    private static final String PAYMENT_ID_NEW = "123";


    @Configuration

    @EntityScan(basePackageClasses = Membership.class)
    @EnableJpaRepositories(basePackageClasses = MembershipRepository.class)
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
            return mock(MailSender.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return mock(PasswordEncoder.class);
        }

        @Bean
        public ScheduleService scheduleService() {
            return mock(ScheduleService.class);
        }


        @Bean
        public UserService userService() {
            return new UserService();
        }

        @Bean
        public MembershipService membershipService() {
            return new MembershipService();
        }

    }

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private UserService userService;

    @Test
    public void findByPaymentId() {
        Membership findByPaymentId = membershipService.findByPaymentId(PAYMENT_ID_OLD);
        assertNotNull(findByPaymentId);
    }

    @Test
    public void addMembership() {
        membershipService.addMembership(ID, PRICE, PAYMENT_ID_NEW);
        Membership membership = membershipService.findByPaymentId(PAYMENT_ID_NEW);
        assertEquals(membership.getId(), 1);
    }

    @Test
    public void successPaymentAndMembershipClient() throws NullPointerException {
        membershipService.successPayment(PAYMENT_ID_OLD);
        Membership membership = userService.findById(ID).getMembership();
        assertNull(membership.getPaymentId());
        Long day = membershipService.membershipClient(membership);
        assertNotNull(day);
    }
}