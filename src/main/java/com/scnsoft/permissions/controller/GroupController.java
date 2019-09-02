package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.security.GroupDTO;
import com.scnsoft.permissions.service.permission.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController(value = "user_permissions")
@RequestMapping("groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public List<GroupDTO> getGroups() {
        return groupService.findAll();
    }

    @GetMapping("{id}")
    public GroupDTO getById(@PathVariable Long id) {
        return groupService.findById(id).orElseThrow(RuntimeException::new);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PostMapping()
    public GroupDTO postGroup(@NotNull @RequestBody GroupDTO groupDTO) {
        groupDTO.setName(groupDTO.getName().toUpperCase());
        return groupService.save(groupDTO);
    }
}