package hr.tvz.volontiraj.repository;

import hr.tvz.volontiraj.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserInfo_Email(String email);
    void deleteByToken(String token);
    void deleteByUserInfo_Email(String email);
}
