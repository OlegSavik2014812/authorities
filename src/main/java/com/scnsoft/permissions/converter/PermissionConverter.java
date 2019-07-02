package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.PermissionDTO;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.Permission;
import com.scnsoft.permissions.persistence.entity.CompositePermissionId;
import com.scnsoft.permissions.persistence.entity.UserGroup;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.persistence.repository.UserGroupRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PermissionConverter implements EntityConverter<Permission, PermissionDTO> {
    private final UserGroupRepository userGroupRepository;
    private final PermissionRepository permissionRepository;
    private final UserConverter userConverter;

    public PermissionConverter(UserGroupRepository userGroupRepository, PermissionRepository permissionRepository, UserConverter userConverter) {
        this.userGroupRepository = userGroupRepository;
        this.permissionRepository = permissionRepository;
        this.userConverter = userConverter;
    }

    @Override
    public PermissionDTO toDTO(Permission entity) {
        if (entity == null) {
            return null;
        }
        PermissionDTO permissionDTO = new PermissionDTO();
        Optional.ofNullable(entity.getId()).ifPresent(permissionDTO::setId);
        permissionDTO.setName(entity.getName());

        List<Long> groupIds = entity.getUserGroups().stream()
                .map(UserGroup::getId)
                .collect(Collectors.toList());
        permissionDTO.setGroupIds(groupIds);

        Map<UserDTO, Boolean> collect = entity.getAdditionalPermissions()
                .stream()
                .collect(Collectors.toMap(permission -> userConverter.toDTO(permission.getUser()),
                        AdditionalPermission::isEnabled));

        permissionDTO.setAdditionalPermissions(collect);
        return permissionDTO;
    }

    @Override
    public Permission toPersistence(PermissionDTO entity) {
        if (entity == null) {
            return null;
        }
        Permission permission = new Permission();
        Optional.ofNullable(entity.getId()).ifPresent(permission::setId);
        permission.setName(entity.getName());

        List<UserGroup> list = new ArrayList<>();
        userGroupRepository.findAll().forEach(list::add);
        permission.setUserGroups(list);
        if (permissionRepository.existsById(entity.getId())) {
            List<AdditionalPermission> collect = entity.getAdditionalPermissions().entrySet().stream()
                    .map(entry ->
                            AdditionalPermission.builder()
                                    .id(new CompositePermissionId(entry.getKey().getId(), entity.getId()))
                                    .user(userConverter.toPersistence(entry.getKey()))
                                    .permission(permission)
                                    .isEnabled(entry.getValue())
                                    .build()
                    ).collect(Collectors.toList());
            permission.setAdditionalPermissions(collect);
        }
        return permission;
    }
}