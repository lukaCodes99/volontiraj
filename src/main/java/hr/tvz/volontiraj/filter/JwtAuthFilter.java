package hr.tvz.volontiraj.filter;

import hr.tvz.volontiraj.configuration.JwtProperties;
import hr.tvz.volontiraj.service.JwtService;
import hr.tvz.volontiraj.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
@AllArgsConstructor
@lombok.extern.slf4j.Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private JwtService jwtService;

    private MyUserDetailsService userDetailService;

    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/")) {
            String token = null;
            String email = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (jwtProperties.getAccessToken().equals(cookie.getName())) {
                        token = cookie.getValue();
                        email = jwtService.extractEmail(token);
                        break;
                    }
                }
            }
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(email);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access token expired");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
