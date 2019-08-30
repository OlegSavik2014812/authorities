package com.scnsoft.permissions.service.permission;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.security.jwt.JwtTokenProvider;
import com.scnsoft.permissions.security.jwt.JwtUser;
import com.scnsoft.permissions.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private static final String LOGIN_KEY = "login";
    private static final String TOKEN_KEY = "token";
    private static final String PERMISSIONS_KEY = "permissions";
    private static final String PATIENT_PERMISSION = "PATIENT";
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public Map<Object, Object> signIn(UserDTO userDTO) {
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();
        return authenticate(login, password);
    }

    public Map<Object, Object> signUp(UserDTO userDTO) {
        String login = userDTO.getLogin();
        if (userService.existByLogin(login)) {
            LOGGER.error("Unable to sign up. User with name: \"{}\" is already exist.", login);
            return Collections.emptyMap();
        }
        String password = userDTO.getPassword();
        userDTO.setPermissions(getValidPermissions(userDTO.getPermissions()));
        userService.save(userDTO);
        return authenticate(login, password);
    }

    private Map<Object, Object> authenticate(String login, String password) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        JwtUser principal = (JwtUser) authenticate.getPrincipal();

        String username = principal.getUsername();
        Collection<String> roleNames = authenticate.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtTokenProvider.createToken(username, roleNames);
        return Map.of(LOGIN_KEY, username, PERMISSIONS_KEY, roleNames, TOKEN_KEY, token);
    }

    private List<String> getValidPermissions(List<String> inputPermissions) {
        if (CollectionUtils.isEmpty(inputPermissions)) {
            return Collections.singletonList(PATIENT_PERMISSION);
        }
        if (!inputPermissions.contains(PATIENT_PERMISSION)) {
            inputPermissions.add(PATIENT_PERMISSION);
        }
        return inputPermissions;
    }
}