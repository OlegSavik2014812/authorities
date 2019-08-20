package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.GroupDTO;
import com.scnsoft.permissions.service.permission.GroupService;
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

    @GetMapping
    public List<GroupDTO> getGroups() {
        return groupService.findAll();
    }

    @GetMapping("{id}")
    public GroupDTO findById(@PathVariable Long id) {
        return groupService.findById(id).orElseThrow(RuntimeException::new);
    }

    @PreAuthorize("hasAnyAuthority('admin','moderator')")
    @PostMapping(path = "assignPermission", params = {"userGroupName", "permissionName"})
    public GroupDTO assignPermission(@RequestParam String userGroupName, @RequestParam String permissionName) {
        return groupService.assignPermission(userGroupName.toUpperCase(), permissionName.toUpperCase())
                .orElseThrow(RuntimeException::new);
    }

    @PreAuthorize("hasAnyAuthority('admin','moderator')")
    @PostMapping()
    public GroupDTO postGroup(@RequestBody GroupDTO groupDTO) {
        groupDTO.setName(groupDTO.getName().toUpperCase());
        groupService.saveEntity(groupDTO);
        return groupService.findByName(groupDTO.getName()).orElseGet(() -> GroupDTO.builder().build());
    }
}