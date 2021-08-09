package ru.skillbox.blog.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.blog.model.User;
import ru.skillbox.blog.repository.UserRepository;
import ru.skillbox.blog.security.UserRole;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByEmailIgnoreCase(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username)));
        Set<? extends GrantedAuthority> authorities;
        if (user.isModerator()) {
            authorities = Stream.of(UserRole.AUTHOR, UserRole.MODERATOR, UserRole.ADMIN)
                    .flatMap(userRole -> userRole.getGrantedAuthorities().stream())
                    .collect(Collectors.toSet());
        } else {
            authorities = UserRole.AUTHOR.getGrantedAuthorities();
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
