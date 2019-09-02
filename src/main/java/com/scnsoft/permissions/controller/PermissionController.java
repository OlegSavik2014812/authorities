package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.dto.security.GroupDTO;
import com.scnsoft.permissions.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("permissions")
@PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping(path = "/user/assign", params = {"userId", "permId", "isEnabled"})
    public UserDTO assignUserPermission(@RequestParam Long userId, @RequestParam Long permId, @RequestParam boolean isEnabled) {
        return permissionService.assignPermissionToUser(userId, permId, isEnabled);
    }

    @GetMapping(path = "/user/release", params = {"userId", "permId", "isEnabled"})
    public UserDTO releaseUserPermission(@RequestParam Long userId, @RequestParam Long permId) {
        return permissionService.releasePermissionFromUser(userId, permId);
    }

    @GetMapping(path = "/group/assign", params = {"groupId", "permissionsId"})
    public GroupDTO assignGroupPermission(@RequestParam Long groupId, @RequestParam Long permissionsId) {
        return permissionService.assignPermissionToGroup(groupId, permissionsId);
    }
}