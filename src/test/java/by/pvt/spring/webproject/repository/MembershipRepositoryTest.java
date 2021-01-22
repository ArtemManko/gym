package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.Membership;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {MembershipRepositoryTest.EmbeddedPostgresContextConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@PropertySource("classpath:application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MembershipRepositoryTest {

    private static final String ID = "PAYID-MAD7XRQ30R08908KS341031T";

    @Configuration
    @EntityScan(basePackageClasses = Membership.class)
    @EnableJpaRepositories(basePackageClasses = MembershipRepository.class)
    public static class EmbeddedPostgresContextConfiguration {

        @Bean
        @Primary
        public DataSource embeddedPG() throws IOException {
            return EmbeddedPostgres.start().getPostgresDatabase();
        }
    }

    @Autowired
    private MembershipRepository membershipRepository;


    @Test
    public void testRepoPresent() {
        assertNotNull(membershipRepository);
    }

    @Test
    public void testCreate_readByPaymentId() {
        // WHEN
        Membership findByPaymentId = membershipRepository.findByPaymentId(ID);
        // THEN
        assertNotNull(findByPaymentId);

    }

//    @BeforeEach
//    public void setUpDB() {
////        User user = new User();
////        user.setId(1L);
////        user.setFirst_name("Artem");
////        user.setLast_name("Manko");
////        user.setPassword("b");
////        user.setUsername("b");
////        user.setUsername("b");
////        user.setBirthday(null);
////        user.setBirthday(null);
////        user.setRoles(Role.ADMIN);
////        user.setLevels(Level.BEGINNER);
////        user.setEmail("mankoartem2@gmail.com");
////        user.setCountry("Bel");
////        user.setCity("Bel");
////        user.setStreet("Bel");
////        user.setActivationCode(null);
////        user.setPhone_number("558588855");
////        user.setGender(true);
//
//        Membership membership = new Membership();
//
//        membership.setId(1L);
//        membership.setPurchase_date(null);
//        membership.setDuration(3);
//        membership.setPrice(50);
//        membership.setActive(null);
//        membership.setPaymentId("PAYID-MAD7XRQ30R08908KS341031T");
////        membership.setUser(user);
//
////        user.setMembership(membership);
////        userRepository.save(user);
//
//        membershipRepository.save(membership);
//
//    }

}