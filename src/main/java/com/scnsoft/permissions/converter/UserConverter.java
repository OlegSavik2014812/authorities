package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.*;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.persistence.repository.UserGroupRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {
    private final UserGroupRepository repository;
    private final PermissionRepository permissionRepository;

    public UserConverter(UserGroupRepository repository, PermissionRepository permissionRepository) {
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
                .map(UserGroup::getId)
                .ifPresent(userDTO::setGroupId);
        Map<String, Boolean> map = new HashMap<>();
        entity.getAdditionalPermissions()
                .forEach(additionalPermission -> map.put(additionalPermission.getPermission().getName(), additionalPermission.isEnabled()));
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

            List<AdditionalPermission> collect = entity.getAdditionalPermissions().entrySet().stream().map(entry -> {
                Permission permission = permissionRepository.findPermissionByName(entry.getKey()).orElse(new Permission());
                CompositePermissionId id = new CompositePermissionId(user.getId(), permission.getId());
                return AdditionalPermission.builder()
                        .id(id)
                        .permission(permission)
                        .user(user)
                        .isEnabled(entry.getValue())
                        .build();
            }).collect(Collectors.toList());

            user.setAdditionalPermissions(collect);
        }
        return user;
    }
}