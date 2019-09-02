package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "{id}")
    public UserDTO getById(@PathVariable(value = "id") Long id) {
        return userService.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping(path = "/user/assign_group", params = {"userId", "groupId"})
    public UserDTO assignGroup(@RequestParam Long userId, @RequestParam Long groupId) {
        return userService.updateGroup(userId, groupId);
    }

    @GetMapping(path = "/user/release_group", params = {"userId"})
    public UserDTO releaseGroup(@RequestParam Long userId) {
        return userService.updateGroup(userId, null);
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