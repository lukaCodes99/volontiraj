package hr.tvz.volontiraj.controller;

import hr.tvz.volontiraj.configuration.JwtProperties;
import hr.tvz.volontiraj.model.RefreshToken;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.service.JwtService;
import hr.tvz.volontiraj.service.RefreshTokenService;
import hr.tvz.volontiraj.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.Cookie;

import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private JwtProperties jwtProperties;

    private static final String LOGIN_URL = "/auth/api/v1/login";
    private static final String REFRESH_TOKEN_URL = "/auth/api/v1/refreshToken";
    private static final String ME_URL = "/auth/api/v1/me";
    private static final String LOGOUT_URL = "/auth/api/v1/logout";

    @BeforeEach
    void setup() {
        reset(userService, authenticationManager, jwtService, refreshTokenService, jwtProperties);
    }

    @Test
    void testAuthenticateAndGetToken_success() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)).isAuthenticated()).thenReturn(true);
        Authentication authenticationMock = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("user@example.com", "password"));
        when(authenticationMock.getPrincipal()).thenReturn(mock(UserDetails.class));
        when(((UserDetails)authenticationMock.getPrincipal()).getUsername()).thenReturn("user@example.com");

        RefreshToken refreshTokenMock = new RefreshToken();
        refreshTokenMock.setId(123L);
        when(refreshTokenService.createRefreshToken("user@example.com")).thenReturn(refreshTokenMock);

        when(jwtService.generateToken("user@example.com")).thenReturn("access-token");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(userEntity);

        when(jwtProperties.getRefreshTokenId()).thenReturn("refreshTokenId");
        when(jwtProperties.getAccessToken()).thenReturn("accessToken");

        String jsonRequest = "{\"email\":\"user@example.com\",\"password\":\"password\"}";

        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(jsonRequest)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("user@example.com")))
                .andExpect(cookie().exists("refreshTokenId"))
                .andExpect(cookie().exists("accessToken"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(refreshTokenService).createRefreshToken("user@example.com");
        verify(jwtService).generateToken("user@example.com");
        verify(userService).findByEmail("user@example.com");
    }

    @Test
    void testAuthenticateAndGetToken_failAuthentication() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        String jsonRequest = "{\"email\":\"user@example.com\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post(LOGIN_URL)
                        .contentType("application/json")
                        .content(jsonRequest)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(refreshTokenService, jwtService, userService);
    }

    @Test
    void testRefreshToken_success() throws Exception {
        when(jwtProperties.getRefreshTokenId()).thenReturn("refreshTokenId");
        when(jwtProperties.getAccessToken()).thenReturn("accessToken");

        RefreshToken refreshTokenMock = new RefreshToken();
        refreshTokenMock.setId(456L);
        refreshTokenMock.setExpiryDate(Instant.now().plusSeconds(3600));

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("user@example.com");

        when(refreshTokenService.findByTokenId(456L)).thenReturn(refreshTokenMock);
        when(userService.findByEmail(any())).thenReturn(userEntity);
        when(jwtService.generateToken(userEntity.getEmail())).thenReturn("new-access-token");

        mockMvc.perform(post(REFRESH_TOKEN_URL)
                        .cookie(new Cookie("refreshTokenId", "456"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("user@example.com")))
                .andExpect(cookie().value("accessToken", "new-access-token"));

        verify(refreshTokenService).findByTokenId(456L);
        verify(jwtService).generateToken(userEntity.getEmail());
        verify(userService).findByEmail(userEntity.getEmail());
    }

    @Test
    void testRefreshToken_expired() throws Exception {
        when(jwtProperties.getRefreshTokenId()).thenReturn("refreshTokenId");

        RefreshToken refreshTokenMock = new RefreshToken();
        refreshTokenMock.setId(456L);
        refreshTokenMock.setExpiryDate(Instant.now().minusSeconds(10)); // expired

        when(refreshTokenService.findByTokenId(456L)).thenReturn(refreshTokenMock);

        mockMvc.perform(post(REFRESH_TOKEN_URL)
                        .cookie(new Cookie("refreshTokenId", "456"))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verify(refreshTokenService).deleteRefreshToken(refreshTokenMock);
    }

    @Test
    void testGetCurrentUser_success() throws Exception {
        when(jwtProperties.getRefreshTokenId()).thenReturn("refreshTokenId");

        RefreshToken refreshTokenMock = new RefreshToken();
        refreshTokenMock.setId(789L);
        refreshTokenMock.setExpiryDate(Instant.now().plusSeconds(3600));

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("currentuser@example.com");

        when(refreshTokenService.findByTokenId(789L)).thenReturn(refreshTokenMock);
        when(userService.findByEmail(any())).thenReturn(userEntity);

        mockMvc.perform(get(ME_URL)
                        .cookie(new Cookie("refreshTokenId", "789")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("currentuser@example.com")));

        verify(refreshTokenService).findByTokenId(789L);
        verify(userService).findByEmail(userEntity.getEmail());
    }

    @Test
    void testGetCurrentUser_expired() throws Exception {
        when(jwtProperties.getRefreshTokenId()).thenReturn("refreshTokenId");

        RefreshToken refreshTokenMock = new RefreshToken();
        refreshTokenMock.setId(789L);
        refreshTokenMock.setExpiryDate(Instant.now().minusSeconds(100));

        when(refreshTokenService.findByTokenId(789L)).thenReturn(refreshTokenMock);

        mockMvc.perform(get(ME_URL)
                        .cookie(new Cookie("refreshTokenId", "789")))
                .andExpect(status().isUnauthorized());

        verify(refreshTokenService).deleteRefreshToken(refreshTokenMock);
    }

    @Test
    void testLogout_success() throws Exception {
        when(jwtProperties.getRefreshTokenId()).thenReturn("refreshTokenId");
        when(jwtProperties.getAccessToken()).thenReturn("accessToken");

        RefreshToken refreshTokenMock = new RefreshToken();
        refreshTokenMock.setId(321L);

        when(refreshTokenService.findByTokenId(321L)).thenReturn(refreshTokenMock);

        mockMvc.perform(post(LOGOUT_URL)
                        .cookie(new Cookie("refreshTokenId", "321"))
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(cookie().maxAge("refreshTokenId", 0))
                .andExpect(cookie().maxAge("accessToken", 0));

        verify(refreshTokenService).deleteRefreshToken(refreshTokenMock);
    }

    @Test
    void testLogout_noCookie() throws Exception {
        when(jwtProperties.getRefreshTokenId()).thenReturn("refreshTokenId");

        mockMvc.perform(post(LOGOUT_URL).with(csrf()))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(refreshTokenService);
    }
}
