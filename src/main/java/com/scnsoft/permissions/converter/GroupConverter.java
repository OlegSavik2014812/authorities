package com.scnsoft.permissions.converter;

import com.google.common.collect.Lists;
import com.scnsoft.permissions.dto.GroupDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        return GroupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .permissionNames(Lists.transform(entity.getPermissions(), Permission::getName))
                .userNames(Lists.transform(entity.getUsers(), User::getLogin))
                .build();
    }

    @Override
    public Group toPersistence(GroupDTO entity) {
        Group group = new Group();

        Optional.ofNullable(entity.getId())
                .ifPresent(group::setId);

        group.setName(entity.getName());

        Predicate<List<String>> isNotEmpty = strings -> !strings.isEmpty();

        Optional.ofNullable(entity.getPermissionNames())
                .filter(isNotEmpty)
                .ifPresent(names -> setUpList(() -> permissionRepository.findPermissionsByNames(names), group::setPermissions));

        Optional.ofNullable(entity.getUserNames())
                .filter(isNotEmpty)
                .ifPresent(names -> setUpList(() -> userRepository.findUsersByNames(names), group::setUsers));

        return group;
    }

    private <T> void setUpList(Supplier<Iterable<T>> supplier, Consumer<List<T>> setter) {
        List<T> entities = Lists.newArrayList(supplier.get());
        setter.accept(entities);
    }
}