package hr.tvz.volontiraj.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
@NoArgsConstructor
public class JwtProperties {

    private String accessToken;
    private String refreshTokenId;
}
