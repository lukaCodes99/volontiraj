package hr.tvz.volontiraj.util;

import hr.tvz.volontiraj.configuration.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

@AllArgsConstructor
public class CookieUtil {


    public static ResponseCookie createAccessTokenCookie(String accessToken, String accessTokenName)
    {
        return ResponseCookie.from(accessTokenName, accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/api")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie createRefreshTokenIdCookie(String refreshTokenId, String refreshTokenName)
    {
        return ResponseCookie.from(refreshTokenName, refreshTokenId)
                .httpOnly(true)
                .secure(true)
                .path("/auth/v1/refresh")
                .maxAge(Duration.ofDays(15))
                .sameSite("Strict")
                .build();
    }

    public static void addAccessTokenCookieToResponse(HttpServletResponse response, String accessToken, String accessTokenName) {
        ResponseCookie cookie = createAccessTokenCookie(accessToken, accessTokenName);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void addRefreshTokenIdCookieToResponse(HttpServletResponse response, String refreshTokenId, String refreshTokenName) {
        ResponseCookie cookie = createRefreshTokenIdCookie(refreshTokenId, refreshTokenName);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


}
