package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class UserConverter implements EntityConverter<User, UserDTO> {
    private final GroupRepository groupRepository;

    @Override
    public UserDTO toDTO(User entity) {
        Optional<Group> entityGroup = Optional.ofNullable(entity.getGroup());
        String groupName = entityGroup.map(Group::getName)
                .orElse(null);

        List<Permission> groupPermissions = entityGroup.map(Group::getPermissions)
                .orElse(Collections.emptyList());

        List<AdditionalPermission> additionalPermissions = Optional.ofNullable(entity.getAdditionalPermissions())
                .orElse(Collections.emptyList());

        return UserDTO.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .groupName(groupName)
                .permissions(merge(groupPermissions, additionalPermissions))
                .build();
    }

    private List<String> merge(List<Permission> groupPermissions, List<AdditionalPermission> additionalPermissions) {
        Map<Permission, Boolean> resolvedAdditionalPermissions = additionalPermissions.stream()
                .collect(toMap(AdditionalPermission::getPermission, AdditionalPermission::isEnabled));

        return Stream.concat(groupPermissions.stream(), resolvedAdditionalPermissions.keySet().stream())
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
    public User toPersistence(UserDTO entityDTO) {
        User user = new User();
        Optional.ofNullable(entityDTO.getId())
                .ifPresent(user::setId);

        user.setLogin(requireNonNull(entityDTO.getLogin()));
        user.setPassword(requireNonNull(entityDTO.getPassword()));

        Optional.ofNullable(entityDTO.getGroupName())
                .filter(s -> !s.trim().isEmpty())
                .flatMap(groupRepository::findGroupByName)
                .ifPresent(user::setGroup);
        return user;
    }
}