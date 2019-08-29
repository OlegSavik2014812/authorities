package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("rights")
@PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
public class RightsController {
    private final UserService userService;

    public RightsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "assignGroup", params = {"id", "groupId"})
    public UserDTO assignGroup(@RequestParam Long id, @RequestParam Long groupId) {
        return userService.updateGroup(id, groupId);
    }

    @GetMapping(path = "releaseGroup", params = {"id"})
    public UserDTO releaseGroup(@RequestParam Long id) {
        return userService.updateGroup(id, null);
    }

    @GetMapping(path = "assignPermission", params = {"userId", "permId", "isEnabled"})
    public UserDTO assignPermission(@RequestParam Long userId, @RequestParam Long permId, @RequestParam boolean isEnabled) {
        return userService.updateAdditionalPermission(userId, permId, isEnabled);
    }

    @GetMapping(path = "releasePermission", params = {"userId", "permId", "isEnabled"})
    public UserDTO releasePermission(@RequestParam Long userId, @RequestParam Long permId) {
        return userService.deletePermission(userId, permId);
    }
}