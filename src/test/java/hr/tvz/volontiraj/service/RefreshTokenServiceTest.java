package hr.tvz.volontiraj.service;


import hr.tvz.volontiraj.model.RefreshToken;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.repository.RefreshTokenRepository;
import hr.tvz.volontiraj.repository.UserRepository;
import hr.tvz.volontiraj.service.implementation.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void createRefreshToken_ShouldReturnRefreshToken() {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);

        RefreshToken existingToken = buildRefreshToken("old-token", user);

        when(refreshTokenRepository.findByUserInfo_Email(email)).thenReturn(Optional.of(existingToken));
        when(userService.findByEmail(email)).thenReturn(user);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken result = refreshTokenService.createRefreshToken(email);

        verify(refreshTokenRepository).delete(existingToken);
        verify(refreshTokenRepository).flush();
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        assertEquals(user, result.getUserInfo());
        assertNotNull(result.getToken());
        assertTrue(result.getExpiryDate().isAfter(Instant.now()));
    }

    @Test
    void createRefreshToken_ShouldWorkIfNoExistingToken() {
        String email = "user@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);

        when(refreshTokenRepository.findByUserInfo_Email(email)).thenReturn(Optional.empty());
        when(userService.findByEmail(email)).thenReturn(user);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken result = refreshTokenService.createRefreshToken(email);

        verify(refreshTokenRepository, never()).delete(any());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        assertEquals(user, result.getUserInfo());
    }

    @Test
    void deleteRefreshToken_ShouldDeleteRefreshToken() {

        RefreshToken existingToken = buildRefreshToken("existing-token", new UserEntity());

        when(refreshTokenRepository.findByToken(existingToken.getToken())).thenReturn(Optional.ofNullable(existingToken));

        refreshTokenService.deleteRefreshToken(existingToken);

        verify(refreshTokenRepository).delete(existingToken);
    }

    @Test
    void deleteRefreshToken_ShouldThrowException_WhenTokenIsNull() {
        String email = "test@example.com";

        RefreshToken refreshToken = RefreshToken.builder()
                .token("not-found-token")
                .build();

        when(refreshTokenRepository.findByUserInfo_Email(email)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                refreshTokenService.deleteRefreshToken(refreshToken));

        assertEquals("Refresh Token is not in DB..!!", exception.getMessage());
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void findByTokenId_ShouldReturnRefreshToken() {
        Long id = 1L;

        RefreshToken existingToken = RefreshToken.builder()
                .token("found-token")
                .build();

        when(refreshTokenRepository.findById(id)).thenReturn(Optional.of(existingToken));

        RefreshToken result = refreshTokenService.findByTokenId(id);

        assertNotNull(result.getToken());
        assertEquals(existingToken, result);
        verify(refreshTokenRepository).findById(id);
    }

    @Test
    void findByTokenId_ShouldThrowException_WhenTokenIsNull() {
        Long id = 1L;

        when(refreshTokenRepository.findById(id)).thenReturn(Optional.empty());

        RefreshToken result = refreshTokenService.findByTokenId(id);

        assertNull(result);
        verify(refreshTokenRepository).findById(id);
    }


    @Test
    void verifyExpiration_ShouldReturnToken() {
        RefreshToken token = RefreshToken.builder()
                .token("valid-token")
                .expiryDate(Instant.now().plusSeconds(60))
                .build();

        RefreshToken result = refreshTokenService.verifyExpiration(token);

        assertEquals(token, result);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void verifyExpiration_ShouldThrowException_WhenTokenIsExpired() {
        RefreshToken token = RefreshToken.builder()
                .token("expired-token")
                .expiryDate(Instant.now().minusSeconds(10)) // expired
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                refreshTokenService.verifyExpiration(token)
        );

        assertEquals(token.getToken() + " Refresh token is expired. Please make a new login..!", exception.getMessage());
        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void deleteByEmail_ShouldDeleteRefreshToken() {
        String email = "user@example.com";

        refreshTokenService.deleteByEmail(email);

        verify(refreshTokenRepository).deleteByUserInfo_Email(email);
    }


    private RefreshToken buildRefreshToken(String token, UserEntity user) {
        return RefreshToken.builder()
                .id(1L)
                .token(token)
                .userInfo(user)
                .expiryDate(Instant.now().plusSeconds(60))
                .build();
    }
}
