package com.scnsoft.permissions.security;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService service;

    public CustomUserDetailsService(UserService service) {
        this.service = service;
    }


    @Override
    public UserDetails loadUserByUsername(String s) {
        UserDTO userDTO = service.findByLogin(s).orElseThrow(NullPointerException::new);
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();
        List<SimpleGrantedAuthority> collect = userDTO.getAdditionalPermissions().keySet()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new User(login, password, collect);
    }
}