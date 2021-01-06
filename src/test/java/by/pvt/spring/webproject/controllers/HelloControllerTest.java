//package by.pvt.spring.webproject.controllers;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@WithUserDetails("a")
//public class HelloControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private HelloController helloController;
//
////    @Test
////    public void helloPageTest() throws Exception {
////        this.mockMvc.perform(get("/hello"))
////                .andDo(print())
////                .andExpect(authenticated())
////                .andExpect(xpath("//*[@id=nav]/li/ul/li[5]/a").string("Schedule List"));
////    }
//}
