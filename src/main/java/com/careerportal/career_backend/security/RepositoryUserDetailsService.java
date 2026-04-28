package com.careerportal.career_backend.security;

import com.careerportal.career_backend.entity.User;
import com.careerportal.career_backend.repository.UserRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RepositoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public RepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String role = user.getRole() == null ? "STUDENT" : user.getRole().toUpperCase();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }
}