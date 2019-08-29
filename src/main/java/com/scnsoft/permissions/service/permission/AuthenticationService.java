package com.scnsoft.permissions.service.permission;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.security.jwt.JwtTokenProvider;
import com.scnsoft.permissions.security.jwt.JwtUser;
import com.scnsoft.permissions.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
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
    private final Pattern pattern;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        pattern = Pattern.compile("^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{0,256}$");
    }

    public Map<Object, Object> signIn(UserDTO userDTO) {
        if (!isUserValid(userDTO)) {
            return Collections.emptyMap();
        }
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();
        return authenticate(login, password);
    }

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

    private boolean isUserValid(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) {
            LOGGER.error("Unable to authenticate user");
            throw new UsernameNotFoundException("User is not presented");
        }
        Predicate<String> isInputValid = inputString -> Strings.isNotBlank(inputString) && pattern.matcher(inputString).matches();
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
        }
        return inputPermissions;
    }
}