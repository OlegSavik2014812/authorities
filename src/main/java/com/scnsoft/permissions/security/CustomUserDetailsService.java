package com.scnsoft.permissions.security;

import com.scnsoft.permissions.dto.GroupDTO;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.Permission;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.service.GroupService;
import com.scnsoft.permissions.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final GroupService groupService;
    private final PermissionRepository permissionRepository;

    public CustomUserDetailsService(UserService userService, GroupService groupService, PermissionRepository permissionRepository) {
        this.userService = userService;
        this.groupService = groupService;
        this.permissionRepository = permissionRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDTO userDTO = userService.findByLogin(username)
                .orElseThrow(NullPointerException::new);
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();

        return new User(login, password, getAvailableUserPermissions(userDTO));
    }

    private Collection<SimpleGrantedAuthority> getAvailableUserPermissions(UserDTO userDTO) {
        Stream<Optional<Permission>> groupPermissions = groupService.findById(userDTO.getId())
                .map(GroupDTO::getPermissionsIds)
                .orElse(Collections.emptyList())
                .stream()
                .map(permissionRepository::findById);

        Stream<Optional<Permission>> additionalPermission = userDTO.getAdditionalPermissions()
                .keySet()
                .stream()
                .map(permissionRepository::findPermissionByName);

        Stream.concat(groupPermissions, additionalPermission)
                .map(opt -> opt.orElse(null))
                .filter(Objects::nonNull)
                .map(Permission::getName)
                .filter(permName -> {
                    for (Map.Entry<String, Boolean> stringBooleanEntry : userDTO.getAdditionalPermissions().entrySet()) {
                        if (permName.equals(stringBooleanEntry.getKey()) && !stringBooleanEntry.getValue()) {
                            return false;
                        }
                    }
                })


        Set<SimpleGrantedAuthority> permissionSet = new HashSet<>();
        permissionSet.addAll(groupPermissions);
        permissionSet.addAll(additionalPermissions);
        return permissionSet;
    }
}