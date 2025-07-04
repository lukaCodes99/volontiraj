package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.model.UserRole;
import hr.tvz.volontiraj.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity applicationUser = userRepository.findByEmail(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(applicationUser.getPassword())
                .roles(applicationUser.getRole().name())
                .build();
    }
}
