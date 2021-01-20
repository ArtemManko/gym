package by.pvt.spring.webproject.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc

public class ForgotPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //Переход на страницу выбора метода для восстоновления пароля
    @Test
    public void forgotPageGet() throws Exception {
        this.mockMvc.perform(get("/forgot-page"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Use Email")));
    }

    //Переход на страницу метода с использованием пароля из Credentials
    @Test
    public void forgotOldPasswordGet() throws Exception {
        this.mockMvc.perform(get("/forgot-oldpassword"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Input your Password")));
    }

    //Переход на страницу метода с использованием пароля c отправление на Email
    @Test
    public void forgotPasswordGet() throws Exception {
        this.mockMvc.perform(get("/forgot"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Input your Email")));
    }
}