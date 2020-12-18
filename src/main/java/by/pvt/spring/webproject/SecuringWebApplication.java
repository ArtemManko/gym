package by.pvt.spring.webproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecuringWebApplication {
//        implements CommandLineRunner {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(SecuringWebApplication.class, args);
    }

//    @Autowired
//    UserRepository userRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        User user = new User("Art", "Art92", "1", true, null);
//        ScheduleWorkout scheduleWorkout = new ScheduleWorkout("AMAT");
//        user.setSchedule_workouts(Arrays.asList(scheduleWorkout));
//        user.setRoles(Collections.singleton(Role.ADMIN));
//        List<User> users = Arrays.asList(user);
//        userRepository.saveAll(users);

    }

//}