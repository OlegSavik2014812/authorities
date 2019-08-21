package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {
    private static final String EMPTY = "";
    private final GroupRepository groupRepository;

    public UserConverter(GroupRepository repository) {
        this.groupRepository = repository;
    }

    @Override
    public UserDTO toDTO(User entity) {
        Optional<Group> optionalGroup = Optional.ofNullable(entity.getGroup());

        String groupName = optionalGroup
                .map(Group::getName)
                .orElse(EMPTY);
        List<Permission> groupPermissions = optionalGroup
                .map(Group::getPermissions)
                .orElse(Collections.emptyList());

        List<String> availableUserPermissions = merge(groupPermissions, entity.getAdditionalPermissions());
        return UserDTO.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .groupName(groupName)
                .permissions(availableUserPermissions)
                .build();
    }

    private List<String> merge(List<Permission> groupPermissions, List<AdditionalPermission> additionalPermissions) {
        Map<String, Boolean> additionalPermissionsNames = Objects.isNull(additionalPermissions) ?
                Collections.emptyMap() :
                additionalPermissions.stream()
                        .collect(Collectors.toMap(additional -> additional.getPermission().getName(), AdditionalPermission::isEnabled));
        Set<String> set = groupPermissions.stream().map(Permission::getName).collect(Collectors.toSet());
        set.addAll(additionalPermissionsNames.keySet());
        return set.stream()
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