package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.service.JwtService;
import hr.tvz.volontiraj.service.RefreshTokenService;
import hr.tvz.volontiraj.util.JwtUtilTest;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;

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
        String email = "user@example.com";
        String badToken = JwtUtilTest.generateTokenWithExpiry(email, Duration.ofMinutes(-5));
        assertNotNull(badToken);
        UserDetails userDetails = User.withUsername(email).password("pass").authorities("USER").build();

        ExpiredJwtException  expiredJwtException = assertThrows(ExpiredJwtException.class, () -> {
            jwtService.validateToken(badToken, userDetails);
        });

        assertTrue(expiredJwtException.getMessage().contains("JWT expired"));
    }

    @Test
    void extractEmail_ShouldReturnCorrectEmail() {
        String email = "user@example.com";
        String token = jwtService.generateToken(email);
        String extractedEmail = jwtService.extractEmail(token);
        assertEquals(email, extractedEmail);
    }
}
