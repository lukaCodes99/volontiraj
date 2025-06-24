package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.service.EmailService;
import hr.tvz.volontiraj.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {

    private final UserService userService;

    public EmailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void notifyVolunteers() {
        userService.getAllEmailsOfVolunteersForHour()
                .forEach(email -> {
                    System.out.println("Sending notification to: " + email);
                });
    }
}
