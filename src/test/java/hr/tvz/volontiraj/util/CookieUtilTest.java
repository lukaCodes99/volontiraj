package hr.tvz.volontiraj.util;

import hr.tvz.volontiraj.configuration.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CookieUtilTest {

    @Autowired
    private JwtProperties jwtProperties;

    @Mock
    private HttpServletResponse response;

    private static final String ACCESS_TOKEN = "my-access-token";
    private static final String REFRESH_TOKEN = "my-refresh-token";

    @Test
    void testCreateAccessTokenCookie() {
        String accessCookieName = jwtProperties.getAccessToken();
        ResponseCookie cookie = CookieUtil.createAccessTokenCookie(ACCESS_TOKEN, accessCookieName);

        assertEquals(accessCookieName, cookie.getName());
        assertEquals(ACCESS_TOKEN, cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/api", cookie.getPath());
        assertEquals(Duration.ofMinutes(15), cookie.getMaxAge());
        assertEquals("Strict", cookie.getSameSite());
    }

    @Test
    void testCreateRefreshTokenCookie() {
        String refreshCookieName = jwtProperties.getRefreshTokenId();
        ResponseCookie cookie = CookieUtil.createRefreshTokenIdCookie(REFRESH_TOKEN, refreshCookieName);

        assertEquals(refreshCookieName, cookie.getName());
        assertEquals(REFRESH_TOKEN, cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/auth/api/v1", cookie.getPath());
        assertEquals(Duration.ofDays(15), cookie.getMaxAge());
        assertEquals("Strict", cookie.getSameSite());
    }


    @Test
    void testAddAccessTokenCookieToResponse() {
        String accessCookieName = jwtProperties.getAccessToken();

        CookieUtil.addAccessTokenCookieToResponse(response, ACCESS_TOKEN, accessCookieName);

        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), contains(accessCookieName));
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), contains(ACCESS_TOKEN));
    }

    @Test
    void testAddRefreshTokenIdCookieToResponse() {
        String refreshCookieName = jwtProperties.getRefreshTokenId();
        CookieUtil.addRefreshTokenIdCookieToResponse(response, REFRESH_TOKEN, refreshCookieName);

        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), contains(refreshCookieName));
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), contains(REFRESH_TOKEN));
    }

    @Test
    void testClearCookie()
    {
        String accessCookieName = jwtProperties.getAccessToken();
        ResponseCookie cookie = CookieUtil.clearCookie(response, accessCookieName, "/api");

        assertEquals(accessCookieName, cookie.getName());
        assertEquals("", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
        assertEquals("/api", cookie.getPath());
        assertEquals(Duration.ZERO, cookie.getMaxAge());
        assertEquals("Strict", cookie.getSameSite());
    }

}
