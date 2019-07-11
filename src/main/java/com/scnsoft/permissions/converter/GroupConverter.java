package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.GroupDTO;
import com.scnsoft.permissions.persistence.entity.Group;
import com.scnsoft.permissions.persistence.entity.Permission;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class GroupConverter implements EntityConverter<Group, GroupDTO> {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public GroupConverter(PermissionRepository permissionRepository, UserRepository userRepository) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GroupDTO toDTO(Group entity) {
        if (entity == null) {
            return null;
        }

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(entity.getId());
        groupDTO.setName(entity.getName());
        groupDTO.setPermissionNames(convert(entity.getPermissions(), Permission::getName));
        groupDTO.setUserNames(convert(entity.getUsers(), User::getLogin));

        return groupDTO;
    }

    @Override
    public Group toPersistence(GroupDTO entity) {
        if (entity == null) {
            return null;
        }
        Group group = new Group();

        Optional.ofNullable(entity.getId())
                .ifPresent(group::setId);
        group.setName(entity.getName());

        List<Permission> permissions = new ArrayList<>();
        permissionRepository
                .findPermissionsByNames(entity.getPermissionNames())
                .forEach(permissions::add);
        group.setPermissions(permissions);

        List<User> users = new ArrayList<>();
        userRepository
                .findUsersByByNames(entity.getUserNames())
                .forEach(users::add);
        group.setUsers(users);

        return group;
    }

    private <T, K> List<T> convert(Iterable<K> source, Function<K, T> converterFunction) {
        return StreamSupport.stream(source.spliterator(), false)
                .filter(Objects::nonNull)
                .map(converterFunction)
                .collect(Collectors.toList());
    }
}