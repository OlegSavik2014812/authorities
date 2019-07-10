package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.*;
import com.scnsoft.permissions.persistence.repository.GroupRepository;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {
    private final GroupRepository groupRepository;
    private final PermissionRepository permissionRepository;

    public UserConverter(GroupRepository repository, PermissionRepository permissionRepository) {
        this.groupRepository = repository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();

        userDTO.setId(entity.getId());
        userDTO.setLogin(entity.getLogin());
        userDTO.setPassword(entity.getPassword());

        Optional.ofNullable(entity.getGroup())
                .map(Group::getName)
                .ifPresent(userDTO::setGroupName);

        Map<String, Boolean> map = new HashMap<>();

        Optional.ofNullable(entity.getAdditionalPermissions())
                .orElse(Collections.emptyList())
                .forEach(additionalPermission ->
                        map.put(additionalPermission.getPermission().getName(),
                                additionalPermission.isEnabled()));
        userDTO.setAdditionalPermissions(map);
        return userDTO;
    }

    @Override
    public User toPersistence(UserDTO entity) {
        if (entity == null) {
            return null;
        }
        User user = new User();
        Optional.ofNullable(entity.getId())
                .ifPresent(user::setId);
        user.setLogin(entity.getLogin());
        user.setPassword(entity.getPassword());

        Optional.ofNullable(entity.getGroupName())
                .flatMap(groupRepository::findUserGroupByName)
                .ifPresent(user::setGroup);
        if (user.getId() != null) {
            List<AdditionalPermission> list =
                    getAdditionalPermissions(user, entity.getAdditionalPermissions());
            user.setAdditionalPermissions(list);
        }
        return user;
    }

    private List<AdditionalPermission> getAdditionalPermissions(User user, Map<String, Boolean> map) {
        Iterable<Permission> permissionsByNames = permissionRepository.findPermissionsByNames(map.keySet());
        List<AdditionalPermission> list = new ArrayList<>();
        permissionsByNames
                .forEach(permission ->
                        AdditionalPermission.builder()
                                .id(new CompositePermissionId(user.getId(), permission.getId()))
                                .user(user)
                                .permission(permission)
                                .isEnabled(map.get(permission.getName()))
                                .build());
        return list;
    }
}