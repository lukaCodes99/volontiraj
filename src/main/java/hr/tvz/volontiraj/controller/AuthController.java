package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.configuration.JwtProperties;
import hr.tvz.volontiraj.dto.AuthRequestDTO;
import hr.tvz.volontiraj.dto.UserDto;
import hr.tvz.volontiraj.mapper.UserMapper;
import hr.tvz.volontiraj.model.RefreshToken;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.service.JwtService;
import hr.tvz.volontiraj.service.RefreshTokenService;
import hr.tvz.volontiraj.service.UserService;
import hr.tvz.volontiraj.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("auth/api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@lombok.extern.slf4j.Slf4j
public class AuthController {

    private final UserService userService;
    private AuthenticationManager authenticationManager;

    private JwtService jwtService;

    private RefreshTokenService refreshTokenService;

    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword()));
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);
            String accessToken = jwtService.generateToken(email);

            CookieUtil.addRefreshTokenIdCookieToResponse(response, String.valueOf(refreshToken.getId()), jwtProperties.getRefreshTokenId());
            CookieUtil.addAccessTokenCookieToResponse(response, accessToken, jwtProperties.getAccessToken());

            UserDto userDto = UserMapper.mapUserToUserDto(userService.findByEmail(email));

            return ResponseEntity.ok(userDto);
        } catch (AuthenticationException e) {
            log.info("AuthenticationException", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<UserDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshTokenId = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> jwtProperties.getRefreshTokenId().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        RefreshToken refreshToken = refreshTokenService.findByTokenId(Long.valueOf(refreshTokenId));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenService.deleteRefreshToken(refreshToken); // optional cleanup
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        UserEntity user = userService.findByEmail(refreshToken.getUserInfo().getEmail());

        String accessToken = jwtService.generateToken(user.getEmail());

        CookieUtil.addAccessTokenCookieToResponse(response, accessToken, jwtProperties.getAccessToken());

        return ResponseEntity.ok(UserMapper.mapUserToUserDto(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(HttpServletRequest request, HttpServletResponse response) {

        String refreshTokenId = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> jwtProperties.getRefreshTokenId().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        RefreshToken refreshToken = refreshTokenService.findByTokenId(Long.valueOf(refreshTokenId));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenService.deleteRefreshToken(refreshToken); // optional cleanup
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        UserEntity user = userService.findByEmail(refreshToken.getUserInfo().getEmail());

        return ResponseEntity.ok(UserMapper.mapUserToUserDto(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenId = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> jwtProperties.getRefreshTokenId().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        RefreshToken refreshToken = refreshTokenService.findByTokenId(Long.valueOf(refreshTokenId));

        refreshTokenService.deleteRefreshToken(refreshToken);

        CookieUtil.clearCookie(response, jwtProperties.getRefreshTokenId(), "/auth/api/v1");
        CookieUtil.clearCookie(response, jwtProperties.getAccessToken(), "/api");

        return ResponseEntity.noContent().build();
    }
}
