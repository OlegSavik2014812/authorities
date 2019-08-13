package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.UserConverter;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.CompositePermissionId;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseCrudService<User, UserDTO, Long> {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserConverter converter;
    private final PermissionRepository permissionRepository;

    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository,
                       GroupRepository groupRepository,
                       UserConverter converter,
                       PermissionRepository permissionRepository, BCryptPasswordEncoder encoder) {
        super(userRepository, converter);
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.converter = converter;
        this.permissionRepository = permissionRepository;
        this.encoder = encoder;
    }

    public void save(UserDTO userDTO) {
        Optional.ofNullable(userDTO)
                .filter(userDTO1 -> !userRepository.existsByLogin(userDTO1.getLogin()))
                .ifPresent(userDTO1 -> {
                    userDTO1.setPassword(encoder.encode(userDTO.getPassword()));
                    saveEntity(userDTO1);
                });
    }

    public Optional<UserDTO> findByLogin(String name) {
        return userRepository.findUserByLogin(name)
                .map(converter::toDTO);
    }

    public void assignGroup(String login, String userGroupName) {
        userRepository.findUserByLogin(login)
                .ifPresent(user1 -> {
                    groupRepository.findGroupByName(userGroupName)
                            .ifPresent(user1::setGroup);
                    userRepository.save(user1);
                });
    }

    public void assignAdditionalPermission(String login, String permissionName, boolean isEnabled) {
        User user = userRepository.findUserByLogin(login).orElseThrow(RuntimeException::new);
        Permission permission = permissionRepository.findPermissionByName(permissionName)
                .orElseGet(() -> {
                    Permission newPermission = new Permission();
                    newPermission.setName(permissionName);
                    permissionRepository.save(newPermission);
                    return permissionRepository.findPermissionByName(permissionName)
                            .orElseThrow(RuntimeException::new);
                });
        user.getAdditionalPermissions()
                .add(AdditionalPermission.builder()
                        .id(new CompositePermissionId(user.getId(), permission.getId()))
                        .user(user)
                        .permission(permission)
                        .isEnabled(isEnabled)
                        .build()
                );
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}