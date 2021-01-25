package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.Order;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PayPalServiceTest.EmbeddedPostgresContextConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@PropertySource("classpath:application-test.properties")
public class PayPalServiceTest {

    private static final Long ID = 1L;
    private static final Integer PRICE = 25;
    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @Configuration
    public static class EmbeddedPostgresContextConfiguration {
        @Bean
        @Primary
        public DataSource embeddedPG() throws IOException {
            return EmbeddedPostgres.start().getPostgresDatabase();
        }

        @Bean
        public APIContext apiContext() {
            APIContext apiContext = new APIContext(
                    "AUx-lRLLVvk7mK3IDL3Hv2VUnkimKDK5uS2i3U6BLFdfKiv15m4Gu_F_oELu_M5BIMLPjBG2j0J7z-KZ",
                    "ECbqaulCnnsVEqRi5R4PFn_nptpmSacXewF-Bu0V4l8qQSMPXQAH5dIlKbsexcBCU-QvV-0xTozE6g6M",
                    "sandbox");
            return apiContext;
        }

        @Bean
        public PayPalService payPalService() {
            return new PayPalService();
        }
    }

    @Autowired
    private PayPalService service;

    @Test
    public void createPayment() throws PayPalRESTException {

        Order order = new Order();
        order.setPrice(PRICE);
        order.setCurrency("USD");
        order.setMethod("PayPal");
        order.setIntent("Sale");
        order.setDescription("");

        Payment payment = service.createPayment(ID, order.getPrice(), order.getCurrency(), order.getMethod(),
                order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
                "http://localhost:8080/" + SUCCESS_URL);
        assertEquals(payment.getState(), "created");
    }
}