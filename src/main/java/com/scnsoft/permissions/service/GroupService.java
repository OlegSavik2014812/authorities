package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.GroupConverter;
import com.scnsoft.permissions.dto.UserGroupDTO;
import com.scnsoft.permissions.persistence.entity.Group;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.persistence.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService extends BaseService<Group, UserGroupDTO, Long> {
    private final GroupRepository groupRepository;
    private final PermissionRepository permissionRepository;
    private final GroupConverter groupConverter;

    public GroupService(GroupRepository groupRepository,
                        PermissionRepository permissionRepository,
                        GroupConverter groupConverter) {
        super(groupRepository, groupConverter);
        this.groupRepository = groupRepository;
        this.permissionRepository = permissionRepository;
        this.groupConverter = groupConverter;
    }

    public Optional<UserGroupDTO> findByName(String userGroupName) {
        return groupRepository.findUserGroupByName(userGroupName).map(groupConverter::toDTO);
    }

    public Optional<UserGroupDTO> assignPermission(String userGroupName, String permissionByName) {
        return groupRepository.findUserGroupByName(userGroupName)
                .map(userGroup1 -> {
                    permissionRepository.findPermissionByName(permissionByName)
                            .ifPresent(permission -> userGroup1.getPermissions().add(permission));
                    return groupRepository.save(userGroup1);
                }).map(groupConverter::toDTO);
    }

    @Override
    public List<UserGroupDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}