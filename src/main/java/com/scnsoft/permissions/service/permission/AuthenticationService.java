package com.scnsoft.permissions.service.permission;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.security.jwt.JwtTokenProvider;
import com.scnsoft.permissions.security.jwt.JwtUserDetailsService;
import com.scnsoft.permissions.service.UserService;
import com.scnsoft.permissions.util.ExecutionTime;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private static final String LOGIN_KEY = "login";
    private static final String TOKEN_KEY = "token";
    private static final String PATIENT_PERMISSION = "PATIENT";
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


    @ExecutionTime
    public Map<Object, Object> signIn(UserDTO userDTO) {
        if (!isUserValid(userDTO)) {
            return Collections.emptyMap();
        }
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();
        return authenticate(login, password);
    }

    @ExecutionTime
    public Map<Object, Object> signUp(UserDTO userDTO) {
        if (!isUserValid(userDTO)) {
            return Collections.emptyMap();
        }
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();
        userDTO.setPermissions(getValidPermissions(userDTO.getPermissions()));
        userService.save(userDTO);
        return authenticate(login, password);
    }

    private Map<Object, Object> authenticate(String login, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        String token = jwtTokenProvider.createToken(userDetails);
        return Map.of(TOKEN_KEY, token, LOGIN_KEY, login);
    }

    private boolean isUserValid(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) {
            LOGGER.error("Unable to authenticate user");
            throw new UsernameNotFoundException("User is not presented");
        }
        Predicate<String> isInputValid = s -> {
            if (Strings.isBlank(s)) {
                return false;
            }
            int length = s.length();
            return length > 5 && length < 255;
        };
        if (isInputValid.negate().test(userDTO.getLogin())) {
            LOGGER.error("Unable to authenticate user, login is not valid");
            return false;
        }
        if (isInputValid.negate().test(userDTO.getPassword())) {
            LOGGER.error("Unable to authenticate user, password is not valid");
            return false;
        }
        return true;
    }

    private List<String> getValidPermissions(List<String> inputPermissions) {
        if (CollectionUtils.isEmpty(inputPermissions)) {
            return Collections.singletonList(PATIENT_PERMISSION);
        }
        if (!inputPermissions.contains(PATIENT_PERMISSION)) {
            inputPermissions.add(PATIENT_PERMISSION);
            return inputPermissions;
        }
        return inputPermissions;
    }
}