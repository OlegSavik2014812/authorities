package com.scnsoft.permissions.service;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.security.jwt.JwtTokenProvider;
import com.scnsoft.permissions.security.jwt.JwtUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public Map<Object, Object> signIn(UserDTO userDTO) {
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        String token = jwtTokenProvider.createToken(userDetails);

        Map<Object, Object> map = new HashMap<>();
        map.put("login", login);
        map.put("token", token);
        return map;
    }

    public Map<Object, Object> signUp(UserDTO userDTO) {
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();
        userService.save(userDTO);
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setLogin(login);
        userDTO1.setPassword(password);
        return signIn(userDTO1);
    }
}
