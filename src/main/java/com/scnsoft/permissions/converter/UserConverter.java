package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {
    private static final String EMPTY = "";
    private final GroupRepository groupRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public UserConverter(GroupRepository repository, PermissionRepository permissionRepository, UserRepository userRepository) {
        this.groupRepository = repository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO toDTO(User entity) {
        String groupName = Optional.ofNullable(entity.getGroup())
                .map(Group::getName).orElse(EMPTY);

        List<Permission> groupPermissions = Optional.ofNullable(entity.getGroup())
                .map(Group::getPermissions)
                .orElse(Collections.emptyList());

        List<AdditionalPermission> additionalPermissions = Optional.ofNullable(entity.getAdditionalPermissions())
                .orElse(Collections.emptyList());

        List<String> availableUserPermissions = merge(groupPermissions, additionalPermissions);
        return UserDTO.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .groupName(groupName)
                .permissions(availableUserPermissions)
                .build();
    }

    private List<String> merge(List<Permission> groupPermissions, List<AdditionalPermission> additionalPermissions) {
        Map<String, Boolean> additionalPermissionsNames = new HashMap<>();
        additionalPermissions.forEach(permission ->
                additionalPermissionsNames.put(permission.getPermission().getName(), permission.isEnabled()));

        List<String> list = groupPermissions.stream().map(Permission::getName).collect(Collectors.toList());

        list.addAll(additionalPermissionsNames.keySet());
        return list.stream()
                .filter(permission -> additionalPermissionsNames.getOrDefault(permission, true))
                .collect(Collectors.toList());
    }

    @Override
    public User toPersistence(UserDTO entity) {
        User user = new User();
        Optional.ofNullable(entity.getId())
                .ifPresent(user::setId);

        user.setLogin(Objects.requireNonNull(entity.getLogin()));
        user.setPassword(Objects.requireNonNull(entity.getPassword()));

        Optional<Group> optionalGroup = Optional.ofNullable(entity.getGroupName())
                .filter(Strings::isNotBlank)
                .flatMap(groupRepository::findGroupByName);

        optionalGroup.ifPresent(user::setGroup);
        return user;
    }
}