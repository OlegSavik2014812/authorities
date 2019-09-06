package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private static final String TOTAL_PAGES = "totalPages";
    private static final String TOTAL_ELEMENTS = "totalElements";
    private static final String USERS = "users";
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

    @GetMapping(params = {"page", "size"})
    public ResponseEntity getAll(@RequestParam int page, @RequestParam int size) {
        Page<UserDTO> all = userService.findAll(page, size);
        return getUsersResponse(all);
    }

    @GetMapping(path = "/permission", params = {"page", "size", "name"})
    public ResponseEntity getByPermission(@RequestParam int page, @RequestParam int size, @RequestParam String name) {
        Page<UserDTO> all = userService.findWithPermission(page, size, List.of(name));
        return getUsersResponse(all);
    }

    @GetMapping(path = "/additional_permission", params = {"page", "size", "name"})
    public ResponseEntity getByAdditionalPermission(@RequestParam int page, @RequestParam int size, @RequestParam String name) {
        Page<UserDTO> all = userService.findWithAssignedPermissions(page, size, List.of(name));
        return getUsersResponse(all);
    }

    private ResponseEntity getUsersResponse(Page<UserDTO> userPage) {
        int totalPages = userPage.getTotalPages();
        long totalElements = userPage.getTotalElements();
        List<UserDTO> content = userPage.getContent();
        Map<String, Object> totalPages1 =
                Map.of(TOTAL_PAGES, totalPages,
                        TOTAL_ELEMENTS, totalElements,
                        USERS, content);
        return ResponseEntity.ok(totalPages1);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("delete/{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        userService.deleteById(id);
    }
}