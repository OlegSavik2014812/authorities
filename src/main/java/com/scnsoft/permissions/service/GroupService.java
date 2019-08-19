package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.GroupConverter;
import com.scnsoft.permissions.dto.GroupDTO;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupService extends BaseCrudService<Group, GroupDTO, Long> {
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

    public Optional<GroupDTO> findByName(String userGroupName) {
        return groupRepository
                .findGroupByName(userGroupName)
                .map(groupConverter::toDTO);
    }

    public Optional<GroupDTO> assignPermission(String userGroupName, String permissionByName) {
        return groupRepository.findGroupByName(userGroupName)
                .map(userGroup1 -> {
                    permissionRepository.findPermissionByName(permissionByName)
                            .ifPresent(permission -> userGroup1.getPermissions().add(permission));
                    return groupRepository.save(userGroup1);
                }).map(groupConverter::toDTO);
    }
}