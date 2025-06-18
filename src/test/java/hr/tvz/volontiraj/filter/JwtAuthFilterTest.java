package hr.tvz.volontiraj.filter;

import hr.tvz.volontiraj.filter.JwtAuthFilter;
import hr.tvz.volontiraj.service.JwtService;
import hr.tvz.volontiraj.service.MyUserDetailsService;
import hr.tvz.volontiraj.configuration.JwtProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_validToken_setsAuthentication() throws Exception {
        String token = "valid.token.here";
        String email = "user@example.com";

        when(request.getRequestURI()).thenReturn("/api/something");
        Cookie cookie = new Cookie("accessToken", token);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        when(jwtProperties.getAccessToken()).thenReturn("accessToken");
        when(jwtService.extractEmail(token)).thenReturn(email);

        UserDetails userDetails = User.withUsername(email).password("pass").authorities(() -> "ROLE_USER").build();
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        SecurityContextHolder.getContext().setAuthentication(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(email, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_invalidToken_throwsUnauthorized() throws Exception {  // <-- ovdje dodano throws Exception
        String token = "invalid.token.here";
        String email = "user@example.com";

        when(request.getRequestURI()).thenReturn("/api/something");
        Cookie cookie = new Cookie("accessToken", token);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        when(jwtProperties.getAccessToken()).thenReturn("accessToken");
        when(jwtService.extractEmail(token)).thenReturn(email);

        UserDetails userDetails = User.withUsername(email).password("pass").authorities(() -> "ROLE_USER").build();
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(false);

        SecurityContextHolder.getContext().setAuthentication(null);

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            jwtAuthFilter.doFilterInternal(request, response, filterChain);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatusCode());
        assertEquals("Access token expired", thrown.getReason());
        verify(filterChain, never()).doFilter(request, response);
    }


    @Test
    void doFilterInternal_noCookies_callsFilterChainWithoutAuthentication() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/something");
        when(request.getCookies()).thenReturn(null);
        when(jwtProperties.getAccessToken()).thenReturn("accessToken");

        SecurityContextHolder.getContext().setAuthentication(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_requestNotApi_callsFilterChainWithoutAuthentication() throws Exception {
        when(request.getRequestURI()).thenReturn("/public/resource");
        SecurityContextHolder.getContext().setAuthentication(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
