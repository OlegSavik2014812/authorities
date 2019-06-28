package com.scnsoft.authorities.controller;

import com.scnsoft.authorities.dto.UserGroupDTO;
import com.scnsoft.authorities.service.UserGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "user_permissions")
@RequestMapping("groups")
public class GroupController {
    private final UserGroupService userGroupService;

    public GroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @GetMapping
    public List<UserGroupDTO> getGroups() {
        return userGroupService.findAll();
    }

    @GetMapping("{id}")
    public UserGroupDTO findById(@PathVariable Long id) {
        return userGroupService.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping(path = "assignAuthority", params = {"userGroupName", "authorityName"})
    public UserGroupDTO assignAuthority(@RequestParam String userGroupName, @RequestParam String authorityName) {
        return userGroupService.assignAuthority(userGroupName, authorityName).orElseThrow(RuntimeException::new);
    }

    @PostMapping()
    public UserGroupDTO postGroup(@RequestBody UserGroupDTO userGroupDTO) {
        return userGroupService.saveEntity(userGroupDTO).orElseThrow(RuntimeException::new);
    }
}
