package hr.tvz.volontiraj.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String generateToken(String subject);
    String extractEmail(String token);
    boolean validateToken(String token, UserDetails userDetails);
}

