package com.scnsoft.permissions.service.permission;

import com.scnsoft.permissions.converter.EntityConverter;
import com.scnsoft.permissions.converter.GroupConverter;
import com.scnsoft.permissions.converter.UserConverter;
import com.scnsoft.permissions.dto.EntityDTO;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.dto.security.GroupDTO;
import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.CompositePermissionId;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
@AllArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final GroupRepository groupRepository;
    private final GroupConverter groupConverter;

    public UserDTO assignPermissionToUser(Long userId, Long permissionId, boolean isEnabled) {
        UnaryOperator<User> assignPermissionAction = user1 -> {
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new NullPointerException("No such entity"));
            user1.getAdditionalPermissions().add(
                    AdditionalPermission.builder()
                            .id(new CompositePermissionId(userId, permissionId))
                            .user(user1)
                            .permission(permission)
                            .isEnabled(isEnabled)
                            .build()
            );
            return user1;
        };
        return executePermissionAction(userRepository, userId, userConverter, assignPermissionAction);
    }

    public UserDTO releasePermissionFromUser(Long userId, Long permissionId) {
        UnaryOperator<User> releasePermissionAction = user1 -> {
            user1.getAdditionalPermissions()
                    .removeIf(additionalPermission -> additionalPermission.getPermission().getId().equals(permissionId));
            return user1;
        };
        return executePermissionAction(userRepository, userId, userConverter, releasePermissionAction);
    }

    public GroupDTO assignPermissionToGroup(Long groupId, Long permissionId) {
        UnaryOperator<Group> assignPermissionAction = group -> {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new NullPointerException("No such permission"));
            group.getPermissions().add(permission);
            return group;
        };
        return executePermissionAction(groupRepository, groupId, groupConverter, assignPermissionAction);
    }

    public GroupDTO releasePermissionFromGroup(Long groupId, Long permissionId) {
        UnaryOperator<Group> releasePermissionAction = group -> {
            group.getPermissions().removeIf(permission1 -> permission1.getId().equals(permissionId));
            return group;
        };
        return executePermissionAction(groupRepository, groupId, groupConverter, releasePermissionAction);
    }

    private <T extends PersistenceEntity<Long>,
            K extends EntityDTO<Long>> K executePermissionAction(CrudRepository<T, Long> entityRepository, Long entityId,
                                                                 EntityConverter<T, K> converter,
                                                                 UnaryOperator<T> permissionAction) {
        T entity = entityRepository.findById(entityId)
                .orElseThrow(() -> new NullPointerException("No such entity"));
        T assignedEntity = permissionAction.apply(entity);
        T savedEntity = entityRepository.save(assignedEntity);
        return converter.toDTO(savedEntity);
    }
}