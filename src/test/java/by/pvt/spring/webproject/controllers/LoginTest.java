//package by.pvt.spring.webproject.controllers;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.core.StringContains.containsString;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class LoginTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void contextLoads() throws Exception {
//        this.mockMvc.perform(get("/"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("Sign in")));
//    }
//
//    @Test
//    public void accessDeniedTest() throws Exception {
//        this.mockMvc.perform((get("/hello")))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("http://localhost/login"));
//    }
//
//    @Test
//    public void correctLogin() throws Exception {
//        this.mockMvc.perform(formLogin().user("a").password("a"))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/hello"));
//
//    }
//
//    @Test
//    public void badCredentials() throws Exception{
//        this.mockMvc.perform(post("/login").param("user", "Nik"))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
//}
