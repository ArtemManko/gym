package by.pvt.spring.webproject.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MailSenderTest {
    private static final String emailTo = "mankoartem2@gmail.com";
    private static final String subject = "Test";
    private static final String message = "Check test";

    @Autowired
    private MailSender service;

    @Test(expected = RuntimeException.class)
    public void sendTest() throws NullPointerException {
        service.send(emailTo, subject, message);
    }
}