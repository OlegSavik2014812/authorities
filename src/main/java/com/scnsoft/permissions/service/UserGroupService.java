package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.UserGroupConverter;
import com.scnsoft.permissions.dto.UserGroupDTO;
import com.scnsoft.permissions.persistence.entity.UserGroup;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.persistence.repository.UserGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserGroupService extends BaseService<UserGroup, UserGroupDTO, Long> {
    private final UserGroupRepository userGroupRepository;
    private final PermissionRepository permissionRepository;
    private final UserGroupConverter groupConverter;

    public UserGroupService(UserGroupRepository userGroupRepository,
                            PermissionRepository permissionRepository,
                            UserGroupConverter groupConverter) {
        super(userGroupRepository, groupConverter);
        this.userGroupRepository = userGroupRepository;
        this.permissionRepository = permissionRepository;
        this.groupConverter = groupConverter;
    }

    public Optional<UserGroupDTO> getByName(String userGroupName) {
        return userGroupRepository.findUserGroupByName(userGroupName).map(groupConverter::toDTO);
    }

    public Optional<UserGroupDTO> assignPermission(String userGroupName, String permissionByName) {
        return userGroupRepository.findUserGroupByName(userGroupName)
                .map(userGroup1 -> {
                    permissionRepository.findPermissionByName(permissionByName)
                            .ifPresent(permission -> userGroup1.getPermissions().add(permission));
                    return userGroupRepository.save(userGroup1);
                }).map(groupConverter::toDTO);
    }

    @Override
    public List<UserGroupDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}