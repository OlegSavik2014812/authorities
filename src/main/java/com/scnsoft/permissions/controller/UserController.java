package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("#")
    public List<UserDTO> getPatients() {
        return null;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("delete/{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
    }
}