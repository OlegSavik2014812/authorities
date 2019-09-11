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

import java.util.List;
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
        UnaryOperator<User> assignPermissionAction = user -> {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new NullPointerException("No such entity"));
            List<AdditionalPermission> additionalPermissions = user.getAdditionalPermissions();
            boolean contains = additionalPermissions.stream()
                    .anyMatch(additionalPermission -> (additionalPermission.getPermission().equals(permission)) && (additionalPermission.isEnabled() == isEnabled));
            if (contains) {
                return user;
            }
            additionalPermissions.add(
                    AdditionalPermission.builder()
                            .id(new CompositePermissionId(userId, permissionId))
                            .user(user)
                            .permission(permission)
                            .isEnabled(isEnabled)
                            .build()
            );
            return user;
        };
        return executePermissionAction(userRepository, userId, assignPermissionAction, userConverter);
    }

    public UserDTO releasePermissionFromUser(Long userId, Long permissionId) {
        UnaryOperator<User> releasePermissionAction = user -> {
            user.getAdditionalPermissions().removeIf(additionalPermission -> additionalPermission.getPermission().getId().equals(permissionId));
            return user;
        };
        return executePermissionAction(userRepository, userId, releasePermissionAction, userConverter);
    }

    public GroupDTO assignPermissionToGroup(Long groupId, Long permissionId) {
        UnaryOperator<Group> assignPermissionAction = group -> {
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new NullPointerException("No such permission"));
            List<Permission> permissions = group.getPermissions();
            if (!permissions.contains(permission)) {
                permissions.add(permission);
            }
            return group;
        };
        return executePermissionAction(groupRepository, groupId, assignPermissionAction, groupConverter);
    }

    public GroupDTO releasePermissionFromGroup(Long groupId, Long permissionId) {
        UnaryOperator<Group> releasePermissionAction = group -> {
            group.getPermissions().removeIf(permission -> permission.getId().equals(permissionId));
            return group;
        };
        return executePermissionAction(groupRepository, groupId, releasePermissionAction, groupConverter);
    }

    private <T extends PersistenceEntity<Long>,
            K extends EntityDTO<Long>> K executePermissionAction(CrudRepository<T, Long> entityRepository, Long entityId,
                                                                 UnaryOperator<T> permissionAction,
                                                                 EntityConverter<T, K> converter) {
        return entityRepository.findById(entityId)
                .map(permissionAction)
                .map(entityRepository::save)
                .map(converter::toDTO)
                .orElseThrow(() -> new NullPointerException("No such entity"));
    }
}