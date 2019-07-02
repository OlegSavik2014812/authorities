package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.UserGroupDTO;
import com.scnsoft.permissions.service.GroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "user_permissions")
@RequestMapping("groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PreAuthorize("hasAuthority('edit-preorder')")
    @GetMapping
    public List<UserGroupDTO> getGroups() {
        return groupService.findAll();
    }

    @GetMapping("{id}")
    public UserGroupDTO findById(@PathVariable Long id) {
        return groupService.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping(path = "assignPermission", params = {"userGroupName", "permissionName"})
    public UserGroupDTO assignPermission(@RequestParam String userGroupName, @RequestParam String permissionName) {
        return groupService.assignPermission(userGroupName, permissionName).orElseThrow(RuntimeException::new);
    }

    @PostMapping()
    public UserGroupDTO postGroup(@RequestBody UserGroupDTO userGroupDTO) {
        return groupService.saveEntity(userGroupDTO).orElseThrow(RuntimeException::new);
    }
}