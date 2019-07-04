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

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
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
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserDTO userDTO = userService.findByLogin(username)
                .orElseThrow(NullPointerException::new);
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();

        return new User(login, password, getAvailableUserPermissions(userDTO));
    }

    private Collection<SimpleGrantedAuthority> getAvailableUserPermissions(UserDTO userDTO) {
        Map<String, Boolean> additionalPermissions = userDTO.getAdditionalPermissions();

        Stream<Optional<Permission>> groupPermissions = groupService.findById(userDTO.getId())
                .map(GroupDTO::getPermissionsIds)
                .orElse(Collections.emptyList())
                .stream()
                .map(permissionRepository::findById);

        Stream<Optional<Permission>> additionalPermission = additionalPermissions
                .keySet()
                .stream()
                .map(permissionRepository::findPermissionByName);

        return Stream.concat(groupPermissions, additionalPermission)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Permission::getName)
                .filter(name -> isValidName(name, additionalPermissions))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private boolean isValidName(String permissionName, Map<String, Boolean> additionalPermissions) {
        for (Map.Entry<String, Boolean> permissionEntry : additionalPermissions.entrySet()) {
            if (permissionEntry.getKey().equals(permissionName) && !permissionEntry.getValue()) {
                return false;
            }
        }
        return true;
    }
}