package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.PermissionConverter;
import com.scnsoft.permissions.dto.PermissionDTO;
import com.scnsoft.permissions.persistence.entity.Permission;
import com.scnsoft.permissions.persistence.entity.UserGroup;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService extends BaseService<Permission, PermissionDTO, Long> {
    private final PermissionRepository permissionRepository;
    private final PermissionConverter permissionConverter;

    public PermissionService(PermissionRepository permissionRepository, PermissionConverter permissionConverter) {
        super(permissionRepository, permissionConverter);
        this.permissionRepository = permissionRepository;
        this.permissionConverter = permissionConverter;
    }

    public PermissionDTO findByName(String name) {
        return permissionRepository.findPermissionByName(name)
                .map(permissionConverter::toDTO)
                .orElseThrow(NullPointerException::new);
    }

    public void addUserGroups(Permission permission, UserGroup userGroup) {
        permissionRepository
                .findPermissionByName(permission.getName())
                .ifPresent(permission1 -> {
                    permission1.getUserGroups().add(userGroup);
                    permissionRepository.save(permission1);
                });
    }
@Override
    public List<PermissionDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}