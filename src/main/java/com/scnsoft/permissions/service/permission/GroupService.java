package com.scnsoft.permissions.service.permission;

import com.scnsoft.permissions.converter.GroupConverter;
import com.scnsoft.permissions.dto.security.GroupDTO;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.UnaryOperator;

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

    public GroupDTO save(GroupDTO groupDTO) {
        return Optional.ofNullable(groupDTO)
                .map(groupConverter::toPersistence)
                .map(groupRepository::save)
                .map(groupConverter::toDTO)
                .orElseThrow(() -> new NullPointerException("Unable to save group"));
    }

    public GroupDTO assignPermission(Long groupId, Long permissionId) {
        UnaryOperator<Group> assignPermission = group -> {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new NullPointerException("No such permission"));
            group.getPermissions().add(permission);
            return group;
        };
        return executePermissionAction(assignPermission, groupId);
    }

    public GroupDTO releasePermission(Long groupId, Long permissionId) {
        UnaryOperator<Group> releasePermission = group -> {
            group.getPermissions().removeIf(permission1 -> permission1.getId().equals(permissionId));
            return group;
        };
        return executePermissionAction(releasePermission, groupId);
    }

    private GroupDTO executePermissionAction(UnaryOperator<Group> action, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NullPointerException("No such group"));
        Group apply = action.apply(group);
        Group save = groupRepository.save(apply);
        return groupConverter.toDTO(save);
    }
}