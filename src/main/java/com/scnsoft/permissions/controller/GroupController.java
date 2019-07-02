package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserGroupDTO;
import com.scnsoft.permissions.service.UserGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "user_permissions")
@RequestMapping("groups")
public class GroupController {
    private final UserGroupService userGroupService;

    public GroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @PreAuthorize("hasAuthority('edit-preorder')")
    @GetMapping
    public List<UserGroupDTO> getGroups() {
        return userGroupService.findAll();
    }

    @GetMapping("{id}")
    public UserGroupDTO findById(@PathVariable Long id) {
        return userGroupService.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping(path = "assignPermission", params = {"userGroupName", "permissionName"})
    public UserGroupDTO assignPermission(@RequestParam String userGroupName, @RequestParam String permissionName) {
        return userGroupService.assignPermission(userGroupName, permissionName).orElseThrow(RuntimeException::new);
    }

    @PostMapping()
    public UserGroupDTO postGroup(@RequestBody UserGroupDTO userGroupDTO) {
        return userGroupService.saveEntity(userGroupDTO).orElseThrow(RuntimeException::new);
    }
}