package com.scnsoft.permissions.security.jwt;

import com.google.common.collect.Iterables;
import com.scnsoft.permissions.dto.GroupDTO;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.Permission;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.service.GroupService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class JwtUserFactory {
    private final GroupService groupService;
    private final PermissionRepository permissionRepository;

    public JwtUserFactory(GroupService groupService, PermissionRepository permissionRepository) {
        this.groupService = groupService;
        this.permissionRepository = permissionRepository;
    }

    public JwtUser create(UserDTO userDTO) {
        return new JwtUser(userDTO.getId(), userDTO.getLogin(), userDTO.getPassword(), true, getAvailableUserPermissions(userDTO));
    }

    private Collection<SimpleGrantedAuthority> getAvailableUserPermissions(UserDTO userDTO) {
        Map<String, Boolean> additionalPermissions = userDTO.getAdditionalPermissions();

        Iterable<Permission> groupPermissions = groupService.findById(userDTO.getGroupId())
                .map(GroupDTO::getPermissionsIds)
                .map(permissionRepository::findAllById)
                .orElseGet(Collections::emptyList);

        Iterable<Permission> additionalPermission = permissionRepository
                .findPermissionsByNames(additionalPermissions.keySet());

        Iterable<Permission> availablePermissions = Iterables.concat(groupPermissions, additionalPermission);

        return StreamSupport.stream(availablePermissions.spliterator(), false)
                .map(Permission::getName)
                .filter(name -> isValidName(name, additionalPermissions))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private static boolean isValidName(String permissionName, Map<String, Boolean> additionalPermissions) {
        for (Map.Entry<String, Boolean> permissionEntry : additionalPermissions.entrySet()) {
            if (permissionEntry.getKey().equals(permissionName)) {
                return permissionEntry.getValue();
            }
        }
        return true;
    }

}
