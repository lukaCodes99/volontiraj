package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.model.RefreshToken;
import hr.tvz.volontiraj.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;
    private UserService userService;

    public RefreshToken createRefreshToken(String email){

        Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUserInfo_Email(email);

        existingRefreshToken.ifPresent(refreshToken -> refreshTokenRepository.deleteByToken(refreshToken.getToken()));

        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userService.findByEmail(email))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if(refreshToken.isPresent()){
            refreshTokenRepository.delete(refreshToken.get());
        } else {
            throw new EntityNotFoundException("Refresh Token is not in DB..!!");
        }
    }

    public RefreshToken findByTokenId(Long id){
        return refreshTokenRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

    public void deleteByUsername(String email) {
        refreshTokenRepository.deleteByUserInfo_Email(email);
    }

}
