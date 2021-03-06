package by.pvt.spring.webproject.controllers;

import by.pvt.spring.webproject.entities.Order;
import by.pvt.spring.webproject.service.MembershipService;
import by.pvt.spring.webproject.service.PayPalService;
import by.pvt.spring.webproject.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','ROLE_USER')")
public class PayPalController {

    @Autowired
    private PayPalService service;
    @Autowired
    private UserService userService;
    @Autowired
    private MembershipService membershipService;

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
        return "block/payMembership/membership";
    }

    @PostMapping("/pay/{id}/{price}")
    public String payment(
            @ModelAttribute("order") Order order,
            @PathVariable("id") Long id_user,
            @PathVariable("price") Integer price
    ) {

        try {
            Payment payment = service.createPayment(id_user, order.getPrice(), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    membershipService.addMembership(id_user, price, payment.getId());
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
        return "block/payMembership/pageAfterPayment";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            Model model) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);

            if (payment.getState().equals("approved")) {
                membershipService.successPayment(payment.getId());
                model.addAttribute("success", "Payment Success");
                return "block/payMembership/pageAfterPayment";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/hello";
    }
}







//    @GetMapping("/membership")
//    public String home2() {
//        return "block/membership";
//    }