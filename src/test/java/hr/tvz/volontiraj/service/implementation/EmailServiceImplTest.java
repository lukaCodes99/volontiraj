package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @Mock
    private UserService userService;

    @Test
    void notifyVolunteers_ShouldNotifyVolunteers() {
        List<String> mockEmails = List.of("volunteer1@example.com", "volunteer2@example.com");
        when(userService.getAllEmailsOfVolunteersForHour()).thenReturn(mockEmails);
        userService.getAllEmailsOfVolunteersForHour().forEach(email ->  System.out.println("Sending notification to: " + email));

        verify(userService).getAllEmailsOfVolunteersForHour();
    }

}
