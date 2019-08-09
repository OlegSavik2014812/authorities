package com.scnsoft.permissions.converter;

import com.google.common.collect.Lists;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.CompositePermissionId;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {
    private static final String EMPTY = "";
    private final GroupRepository groupRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserConverter.class);

    public UserConverter(GroupRepository repository, PermissionRepository permissionRepository, UserRepository userRepository) {
        this.groupRepository = repository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        String groupName = Optional.ofNullable(entity.getGroup())
                .map(Group::getName).orElse(EMPTY);

        LOGGER.error("PERMISSIONS----------------------------");
        List<String> availableUserAuthorities = getAvailableUserAuthorities(entity);
        LOGGER.error("PERMISSIONS----------------------------1111111");
        return UserDTO.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .groupName(groupName)
                .permissions(availableUserAuthorities)
                .build();
    }

    private List<String> getAvailableUserAuthorities(User user) {
        Map<String, Boolean> collect = Optional.ofNullable(user.getAdditionalPermissions())
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(additionalPermission -> additionalPermission.getPermission().getName(), AdditionalPermission::isEnabled));

        List<String> groupPermissions = Optional.ofNullable(user.getGroup())
                .map(Group::getPermissions)
                .map(permissionList -> Lists.transform(permissionList, Permission::getName))
                .orElseGet(ArrayList::new);
        Set<String> set = new HashSet<>();
        set.addAll(collect.keySet());
        set.addAll(groupPermissions);

        return set.stream()
                .filter(permission -> isPermissionSupported(permission, collect))
                .collect(Collectors.toList());
    }

    private boolean isPermissionSupported(String permission, Map<String, Boolean> additionalPermissions) {
        for (Map.Entry<String, Boolean> permissionEntry : additionalPermissions.entrySet()) {
            if (permissionEntry.getKey().equals(permission)) {
                return permissionEntry.getValue();
            }
        }
        return true;
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
        Group group = Optional.ofNullable(entity.getGroupName())
                .flatMap(groupRepository::findUserGroupByName).orElse(null);

        user.setGroup(group);

        List<String> groupPermissions = Optional.ofNullable(group)
                .map(Group::getPermissions)
                .orElse(Collections.emptyList())
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toList());

        User save = userRepository.save(user);
        if (save.getId() != null) {
            List<AdditionalPermission> list = getAdditionalPermissions(save, groupPermissions, entity.getPermissions());
            user.setAdditionalPermissions(list);
        }
        return save;
    }

    private List<AdditionalPermission> getAdditionalPermissions(User user, List<String> groupPermissions, List<String> allPermissions) {
        List<AdditionalPermission> list = new ArrayList<>();

        Map<String, Boolean> map = new HashMap<>();
        for (String allPermission : allPermissions) {
            map.put(allPermission, true);
        }
        for (String groupPermission : groupPermissions) {
            if (map.containsKey(groupPermission)) {
                map.remove(groupPermission);
            } else {
                map.put(groupPermission, false);
            }
        }

        Iterable<Permission> permissionsByNames = permissionRepository.findPermissionsByNames(map.keySet());
        permissionsByNames
                .forEach(permission -> {
                    AdditionalPermission additionalPermission = AdditionalPermission.builder()
                            .id(new CompositePermissionId(user.getId(), permission.getId()))
                            .user(user)
                            .permission(permission)
                            .isEnabled(map.get(permission.getName()))
                            .build();
                    list.add(additionalPermission);
                });
        return list;
    }
}