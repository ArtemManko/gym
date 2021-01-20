package by.pvt.spring.webproject.repository;

import by.pvt.spring.webproject.entities.ScheduleWorkout;
import by.pvt.spring.webproject.entities.enums.Level;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = {MembershipRepositoryTest.EmbeddedPostgresContextConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@PropertySource("classpath:application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ScheduleRepositoryTest {



    @Configuration
    @EntityScan(basePackageClasses = ScheduleWorkout.class)
    @EnableJpaRepositories(basePackageClasses = ScheduleRepository.class)
    public static class EmbeddedPostgresContextConfiguration {

        @Bean
        @Primary
        public DataSource embeddedPG() throws IOException {
            return EmbeddedPostgres.start().getPostgresDatabase();
        }
    }

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void findByLevelsIn() {
        // WHEN
        Set<ScheduleWorkout> findByPaymentId = scheduleRepository.findByLevelsIn(Collections.singleton(Level.BEGINNER));

        // THEN
        assertNotNull(findByPaymentId);
    }

}