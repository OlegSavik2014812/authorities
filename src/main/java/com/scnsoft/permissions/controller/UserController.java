package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.permission.AuthenticationService;
import com.scnsoft.permissions.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("signIn")
    public ResponseEntity signIn(@RequestBody UserDTO user) {
        Map<Object, Object> map = authenticationService.signIn(user);
        return ResponseEntity.ok(map);
    }

    @GetMapping(value = "{id}")
    public UserDTO getById(@PathVariable(value = "id") Long id) {
        return userService.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("user")
    public UserDTO getByLogin(@RequestParam(value = "name") String login) {
        return userService.findByLogin(login).orElseThrow(RuntimeException::new);
    }

    @GetMapping()
    public List<UserDTO> getAll() {
        return userService.findAll();
    }

    @GetMapping
    public List<UserDTO> getPatients(){return null;}

    @PostMapping("signUp")
    public ResponseEntity signUp(@RequestBody UserDTO user) {
        Map<Object, Object> map = authenticationService.signUp(user);
        return ResponseEntity.ok(map);
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("delete/{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('admin','moderator')")
    @GetMapping(path = "assignGroup", params = {"login", "groupNames"})
    public UserDTO assignGroup(@RequestParam String login, @RequestParam String groupName) {
        userService.assignGroup(login, groupName.toUpperCase());
        return userService.findByLogin(login).orElseThrow(RuntimeException::new);
    }

    @PreAuthorize("hasAnyAuthority('admin','moderator')")
    @GetMapping(path = "assignPermission", params = {"login", "groupNames"})
    public UserDTO assignPermission(@RequestParam String login,
                                    @RequestParam String permissionName,
                                    @RequestParam boolean isEnabled) {
        userService.assignAdditionalPermission(login, permissionName.toUpperCase(), isEnabled);
        return userService.findByLogin(login).orElseThrow(RuntimeException::new);
    }
}