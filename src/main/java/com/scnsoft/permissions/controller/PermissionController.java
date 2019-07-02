package com.scnsoft.permissions.controller;

import com.scnsoft.permissions.dto.PermissionDTO;
import com.scnsoft.permissions.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("get")
    public List<PermissionDTO> getAuthorities() {
        return permissionService.findAll();
    }

    @PostMapping("postPermission")
    public PermissionDTO postAuthority(@RequestBody PermissionDTO permissionDTO) {
        return permissionService.saveEntity(permissionDTO).orElseThrow(RuntimeException::new);
    }

    @GetMapping("{id}")
    public PermissionDTO getById(@RequestParam Long id) {
        return permissionService.findById(id).orElseThrow(RuntimeException::new);
    }
}