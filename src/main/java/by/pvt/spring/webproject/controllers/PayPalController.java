package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.Order;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.service.PayPalService;
import by.pvt.spring.webproject.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','COACH')")//change latter
public class PayPalController {

    @Autowired
    private PayPalService service;
    @Autowired
    private UserService userService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @GetMapping("/membership/{id}/{price}")
    public String home(
            @PathVariable("id") Long id_user,
            @PathVariable("price") Integer price,
            Model model
    ) {
        
        model.addAttribute("user", userService.findById(id_user));
        model.addAttribute("price", price);
        return "block/membership";
    }

    @GetMapping("/membership")
    public String home2() {
        return "block/membership";
    }

    @PostMapping("/pay/{id}/{price}")
    public String payment(
            @ModelAttribute("order") Order order,
            @PathVariable("id") Long id_user,
            @PathVariable("price") Integer price,
            Model model) {
        User user = userService.findById(id_user);

        try {
            Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    service.addMembership(id_user, price);
//                    membership.setUser(user);

                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        return "redirect:/hello";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay(
            Model model) {
        model.addAttribute("cancel", "Payment Failure");
        return "block/pageAfterPayment";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            Model model) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
//            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {

                model.addAttribute("success", "Payment Success");
                return "block/pageAfterPayment";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/hello";
    }

}
