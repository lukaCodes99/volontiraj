package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.configuration.JwtProperties;
import hr.tvz.volontiraj.dto.AuthRequestDTO;
import hr.tvz.volontiraj.model.RefreshToken;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.service.JwtService;
import hr.tvz.volontiraj.service.RefreshTokenService;
import hr.tvz.volontiraj.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@lombok.extern.slf4j.Slf4j
public class AuthController {

    private AuthenticationManager authenticationManager;

    private JwtService jwtService;

    private RefreshTokenService refreshTokenService;

    private final JwtProperties jwtProperties;

    @PostMapping("/api/v1/login")
    public ResponseEntity<Void> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {

            String email = ((UserDetails) authentication.getPrincipal()).getUsername();

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);
            String accessToken = jwtService.generateToken(email);

            CookieUtil.addRefreshTokenIdCookieToResponse(response, String.valueOf(refreshToken.getId()), jwtProperties.getRefreshTokenId());
            CookieUtil.addAccessTokenCookieToResponse(response, accessToken, jwtProperties.getAccessToken());

            return ResponseEntity.noContent().build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/api/v1/refreshToken")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshTokenId = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> jwtProperties.getRefreshTokenId().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        RefreshToken oldToken = refreshTokenService.findByTokenId(Long.valueOf(refreshTokenId));
        UserEntity user = oldToken.getUserInfo();

        refreshTokenService.deleteRefreshToken(refreshTokenId);

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        String accessToken = jwtService.generateToken(user.getEmail());

        CookieUtil.addRefreshTokenIdCookieToResponse(response, String.valueOf(newRefreshToken.getId()), jwtProperties.getRefreshTokenId());
        CookieUtil.addAccessTokenCookieToResponse(response, accessToken, jwtProperties.getAccessToken());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtil.clearCookie(response,jwtProperties.getRefreshTokenId(), "/auth/api/v1/refresh");
        CookieUtil.clearCookie(response,jwtProperties.getAccessToken(), "/api");
        return ResponseEntity.noContent().build();
    }

}
