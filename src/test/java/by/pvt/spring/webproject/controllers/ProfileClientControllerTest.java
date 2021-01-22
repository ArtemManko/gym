//package by.pvt.spring.webproject.controllers;
//
//import by.pvt.spring.webproject.config.TestConfig;
//import by.pvt.spring.webproject.entities.User;
//import by.pvt.spring.webproject.service.ScheduleService;
//import by.pvt.spring.webproject.service.UserService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import org.springframework.ui.Model;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = TestConfig.class,
//        loader = AnnotationConfigContextLoader.class)
//public class ProfileClientControllerTest {
//
//    @Autowired
//    private ProfileClientController profileClientController;
//
//
//    //mock dependencies
//    @Autowired
//    private ScheduleService scheduleService;
//
//    @Autowired
//    private UserService userService;
//
//    private static final String userEmail = "mankoartem2@gmail.com";
//    private static final Long userId = 1L;
//    private static final User user = mock(User.class);
//    private Model model;
//
//    @BeforeAll
//    public static void setup(){
//        when(user.getEmail()).thenReturn(userEmail);
//        when(user.getId()).thenReturn(userId);
//    }
//
//    @Test
//    public void clientProfile() {
//        profileClientController.clientProfile(userId,model);
////        verify()
//
//    }
//
//    @Test
//    void editClientForm() {
//    }
//
//    @Test
//    void editClient() {
//    }
//
//    @Test
//    void schedulesList() {
//    }
//
//    @Test
//    void deleteClientSchedule() {
//    }
//}