package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.*;
import com.scnsoft.permissions.persistence.repository.GroupRepository;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {
    private final GroupRepository repository;
    private final PermissionRepository permissionRepository;

    public UserConverter(GroupRepository repository, PermissionRepository permissionRepository) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        Optional.ofNullable(entity.getId())
                .ifPresent(userDTO::setId);
        userDTO.setId(entity.getId());
        userDTO.setLogin(entity.getLogin());
        userDTO.setPassword(entity.getPassword());

        Optional.ofNullable(entity.getGroup())
                .map(Group::getId)
                .ifPresent(userDTO::setGroupId);
        Map<String, Boolean> map = new HashMap<>();
        entity.getAdditionalPermissions()
                .forEach(additionalPermission ->
                        map.put(additionalPermission.getPermission().getName(), additionalPermission.isEnabled()));
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

        Optional.ofNullable(entity.getGroupId())
                .flatMap(repository::findById)
                .ifPresent(user::setGroup);
        if (repository.existsById(entity.getId())) {
            Map<String, Boolean> map = entity.getAdditionalPermissions();
            List<AdditionalPermission> collect = map.entrySet().stream().map(stringBooleanEntry ->
                    build(user, stringBooleanEntry.getKey(), stringBooleanEntry.getValue())).collect(Collectors.toList());
            user.setAdditionalPermissions(collect);
        }
        return user;
    }

    private AdditionalPermission build(User user, String permName, boolean isEnabled) {
        AdditionalPermission additionalPermission = new AdditionalPermission();
        Permission permission = permissionRepository.findPermissionByName(permName).orElse(new Permission());
        CompositePermissionId id = new CompositePermissionId();
        id.setUserId(user.getId());
        id.setPermissionId(permission.getId());
        additionalPermission.setId(id);
        additionalPermission.setPermission(permission);
        additionalPermission.setUser(user);
        additionalPermission.setEnabled(isEnabled);
        return additionalPermission;
    }
}