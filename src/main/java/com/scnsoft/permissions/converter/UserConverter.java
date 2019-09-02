package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class UserConverter implements EntityConverter<User, UserDTO> {
    private final GroupRepository groupRepository;

    @Override
    public UserDTO toDTO(User entity) {
        Group group = entity.getGroup();
        String groupName = null;
        List<Permission> groupPermissions = null;
        List<AdditionalPermission> additionalPermissions = null;
        if (nonNull(group)) {
            groupName = group.getName();
            groupPermissions = group.getPermissions();
            additionalPermissions = entity.getAdditionalPermissions();
        }
        List<String> availableUserPermissions = merge(groupPermissions, additionalPermissions);
        return UserDTO.builder()
                .id(entity.getId())
                .login(requireNonNull(entity.getLogin()))
                .password(requireNonNull(entity.getPassword()))
                .groupName(groupName)
                .permissions(availableUserPermissions)
                .build();
    }

    private List<String> merge(List<Permission> groupPermissions, List<AdditionalPermission> additionalPermissions) {
        Stream<Permission> groupPermissionsStream = nonNull(groupPermissions) ?
                groupPermissions.stream() : Stream.empty();

        Map<Permission, Boolean> resolvedAdditionalPermissions = nonNull(additionalPermissions) ?
                additionalPermissions.stream()
                        .collect(toMap(AdditionalPermission::getPermission, AdditionalPermission::isEnabled)) : emptyMap();
        return Stream.concat(groupPermissionsStream, resolvedAdditionalPermissions.keySet().stream())
                .filter(distinctBy(Permission::getName))
                .filter(permission -> resolvedAdditionalPermissions.getOrDefault(permission, true))
                .map(Permission::getName)
                .collect(toList());
    }

    private Predicate<Permission> distinctBy(Function<Permission, ?> valueExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return permission -> seen.putIfAbsent(valueExtractor.apply(permission), Boolean.TRUE) == null;
    }

    @Override
    public User toPersistence(UserDTO entity) {
        User user = new User();
        Optional.ofNullable(entity.getId())
                .ifPresent(user::setId);

        user.setLogin(requireNonNull(entity.getLogin()));
        user.setPassword(requireNonNull(entity.getPassword()));

        Optional.ofNullable(entity.getGroupName())
                .filter(s -> !s.trim().isEmpty())
                .flatMap(groupRepository::findGroupByName)
                .ifPresent(user::setGroup);
        return user;
    }
}