package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.Membership;
import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private UserService userService;

    public Membership findByPaymentId(String id) {
        return membershipRepository.findByPaymentId(id);
    }

    //Add membership Data Base
    public void addMembership(Long id_user, Integer price, String id) {
        User user = userService.findById(id_user);
        Membership membership = new Membership();
        membership.setPrice(price);
        membership.setPaymentId(id);
        switch (price) {
            case (10):
                membership.setDuration(1);
                break;
            case (25):
                membership.setDuration(3);
                break;
            case (50):
                membership.setDuration(6);
                break;
        }
        membership.setUser(user);
        user.setMembership(membership);
    }

    //If payment success, membership start
    public void successPayment(String id) {
        Membership membership = findByPaymentId(id);
        membership.setPaymentId(null);
        membership.setActive(true);
        membership.setPurchase_date(new Date());
    }

    public long membershipClient(Membership membership) {

        long daysPayment = membership.getPurchase_date().getTime() / (24 * 60 * 60 * 1000);

        switch (membership.getDuration()) {
            case (1):
                daysPayment = daysPayment + 30;
                break;
            case (3):
                daysPayment = daysPayment + 90;
                break;
            case (6):
                daysPayment = daysPayment + 180;
                break;
        };
        long dayLeft = daysPayment - (new Date().getTime() / (24 * 60 * 60 * 1000));
        if( dayLeft <= 0){
            dayLeft = 0;
            membership.setActive(false);
            return dayLeft;
        }
        return dayLeft;
    }
}
