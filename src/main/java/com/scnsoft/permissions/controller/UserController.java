package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.security.jwt.JwtTokenProvider;
import com.scnsoft.permissions.security.jwt.JwtUserDetailsService;
import com.scnsoft.permissions.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final JwtUserDetailsService userDetailsService;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          JwtUserDetailsService userDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("signIn")
    public ResponseEntity signIn(@RequestBody UserDTO userDTO) {
        return authenticate(userDTO);
    }

    @GetMapping("{id}")
    public UserDTO getByName(@RequestParam String login) {
        return userService.findByLogin(login).orElseThrow(RuntimeException::new);
    }

    @PostMapping("signUp")
    public ResponseEntity signUp(@RequestBody UserDTO user) {
        UserDTO userDTO = userService.signUp(user).orElseThrow(RuntimeException::new);
        return authenticate(userDTO);
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("delete/{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('admin','moderator')")
    @GetMapping(path = "assignGroup", params = {"login", "groupNames"})
    public UserDTO assignGroup(@RequestParam String login, @RequestParam String groupName) {
        userService.assignGroup(login, groupName);
        return userService.findByLogin(login).orElseThrow(RuntimeException::new);
    }

    @PreAuthorize("hasAnyAuthority('admin','moderator')")
    @GetMapping(path = "assignPermission", params = {"login", "groupNames"})
    public UserDTO assignPermission(@RequestParam String login,
                                    @RequestParam String permissionName,
                                    @RequestParam boolean isEnabled) {
        userService.assignAdditionalPermission(login, permissionName, isEnabled);
        return userService.findByLogin(login).orElseThrow(RuntimeException::new);
    }

    private ResponseEntity authenticate(UserDTO userDTO) {
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        String token = jwtTokenProvider.createToken(userDetails);

        Map<Object, Object> map = new HashMap<>();
        map.put("login", login);
        map.put("token", token);
        return ResponseEntity.ok(map);
    }
}