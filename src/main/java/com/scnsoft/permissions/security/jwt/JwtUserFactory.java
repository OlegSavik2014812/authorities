package com.scnsoft.permissions.security.jwt;

import com.scnsoft.permissions.dto.UserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUserFactory {
    public JwtUser build(UserDTO userDTO) {
        return JwtUser.builder()
                .id(userDTO.getId())
                .username(userDTO.getLogin())
                .password(userDTO.getPassword())
                .isEnabled(true)
                .authorities(getAvailableUserAuthorities(userDTO))
                .build();
    }

    private Collection<SimpleGrantedAuthority> getAvailableUserAuthorities(UserDTO userDTO) {
        List<SimpleGrantedAuthority> collect = userDTO.getPermissions()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return collect;
    }
}