package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.UserGroupDTO;
import com.scnsoft.permissions.persistence.entity.Permission;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.UserGroup;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserGroupConverter implements EntityConverter<UserGroup, UserGroupDTO> {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public UserGroupConverter(PermissionRepository permissionRepository, UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserGroupDTO toDTO(UserGroup entity) {
        if (entity == null) {
            return null;
        }

        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(entity.getId());
        userGroupDTO.setName(entity.getName());
        userGroupDTO.setPermissionsIds(getList(entity.getPermissions(), Permission::getId));
        userGroupDTO.setUserIds(getList(entity.getUsers(), User::getId));
        return userGroupDTO;
    }

    @Override
    public UserGroup toPersistence(UserGroupDTO entity) {
        if (entity == null) {
            return null;
        }
        UserGroup userGroup = new UserGroup();
        Optional.ofNullable(entity.getId())
                .ifPresent(userGroup::setId);
        userGroup.setName(entity.getName());

        userGroup.setPermissions(getList(getList(
                entity.getPermissionsIds(),
                permissionRepository::findById),
                optional -> optional.orElse(null)));

        userGroup.setUsers(getList(getList(
                entity.getUserIds(),
                userRepository::findById),
                optional -> optional.orElse(null)));

        return userGroup;
    }

    private static <T, K> List<T> getList(Iterable<K> source, Function<K, T> mapper) {
        return StreamSupport.stream(source.spliterator(), false)
                .filter(Objects::nonNull)
                .map(mapper)
                .collect(Collectors.toList());
    }
}