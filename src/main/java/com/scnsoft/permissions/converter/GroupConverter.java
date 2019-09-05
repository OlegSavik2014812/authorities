package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.security.GroupDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupConverter implements EntityConverter<Group, GroupDTO> {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Override
    public GroupDTO toDTO(Group entity) {

        return GroupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .permissionNames(transform(entity.getPermissions(), Permission::getName))
                .userNames(transform(entity.getUsers(), User::getLogin))
                .build();
    }

    private <T> List<String> transform(List<T> entities, Function<T, String> function) {
        if (Objects.isNull(entities) || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream().map(function).collect(Collectors.toList());
    }

    @Override
    public Group toPersistence(GroupDTO entityDTO) {
        Group group = new Group();

        Optional.ofNullable(entityDTO.getId())
                .ifPresent(group::setId);
        group.setName(entityDTO.getName());

        setUpList(entityDTO.getPermissionNames(), permissionRepository::findPermissionsByNames, group::setPermissions);
        setUpList(entityDTO.getUserNames(), userRepository::findUsersByNames, group::setUsers);

        return group;
    }

    private <T> void setUpList(List<String> stringEntitySource,
                               Function<Iterable<String>, List<T>> stringEntityMapper,
                               Consumer<List<T>> entityConsumer) {
        Optional.ofNullable(stringEntitySource)
                .filter(strings -> !strings.isEmpty())
                .map(stringEntityMapper)
                .ifPresent(entityConsumer);
    }
}