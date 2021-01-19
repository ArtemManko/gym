package by.pvt.spring.webproject.controllers;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;


    // Перенаправление на страницу логина, если аноним
    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform((get("/hello")))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    //Запрет на вход (403), если у пользователя неверный данные
    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login").param("user", "Nik"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


}
