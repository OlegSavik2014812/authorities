package com.scnsoft.permissions.security.jwt;

import com.scnsoft.permissions.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    private final JwtUserFactory jwtUserFactory;

    public JwtUserDetailsService(UserService userService, JwtUserFactory jwtUserFactory) {
        this.userService = userService;
        this.jwtUserFactory = jwtUserFactory;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        return userService.findByLogin(username)
                .map(jwtUserFactory::build)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + username + "not found"));
    }
}