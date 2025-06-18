package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.service.JwtService;
import hr.tvz.volontiraj.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private RefreshTokenService refreshTokenService = Mockito.mock(RefreshTokenService.class);

    @BeforeEach
    void setup() {
        jwtService = new JwtService(refreshTokenService);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String email = "user@example.com";
        String token = jwtService.generateToken(email);
        assertNotNull(token);

        UserDetails userDetails = User.withUsername(email).password("pass").authorities("USER").build();

        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    void validateToken_ShouldReturnFalse_ForInvalidToken() {
        String badToken = "invalid.token.here";
        UserDetails userDetails = User.withUsername("user@example.com").password("pass").authorities("USER").build();

        assertFalse(jwtService.validateToken(badToken, userDetails));
    }

    @Test
    void extractEmail_ShouldReturnCorrectEmail() {
        String email = "user@example.com";
        String token = jwtService.generateToken(email);
        String extractedEmail = jwtService.extractEmail(token);
        assertEquals(email, extractedEmail);
    }
}
