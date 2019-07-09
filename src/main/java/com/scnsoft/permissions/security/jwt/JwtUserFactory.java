package com.scnsoft.permissions.security.jwt;

import com.scnsoft.permissions.dto.GroupDTO;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.service.GroupService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUserFactory {
    private final GroupService groupService;

    public JwtUserFactory(GroupService groupService) {
        this.groupService = groupService;
    }

    public JwtUser build(UserDTO userDTO) {
        return JwtUser.builder()
                .id(userDTO.getId())
                .username(userDTO.getLogin())
                .password(userDTO.getPassword())
                .isEnabled(true)
                .authorities(getAvailableUserAuthorities(userDTO))
                .build();
    }

    private Collection<SimpleGrantedAuthority> getAvailableUserAuthorities(UserDTO userDTO) {
        Map<String, Boolean> permissionMap = Optional.ofNullable(userDTO.getAdditionalPermissions())
                .orElse(Collections.emptyMap());

        List<String> userPermissionsNames = groupService.findByName(userDTO.getGroupName())
                .map(GroupDTO::getPermissionsNames)
                .map(groupPermissionNames -> {
                    Set<String> additionalPermissionNames = permissionMap.keySet();
                    groupPermissionNames.addAll(additionalPermissionNames);
                    return groupPermissionNames;
                })
                .filter(list -> !list.isEmpty())
                .orElse(Collections.emptyList());

        return userPermissionsNames
                .stream()
                .filter(permissionName -> isPermissionSupported(permissionName, permissionMap))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private boolean isPermissionSupported(String permissionName, Map<String, Boolean> additionalPermissions) {
        for (Map.Entry<String, Boolean> permissionEntry : additionalPermissions.entrySet()) {
            if (permissionEntry.getKey().equals(permissionName)) {
                return permissionEntry.getValue();
            }
        }
        return true;
    }
}