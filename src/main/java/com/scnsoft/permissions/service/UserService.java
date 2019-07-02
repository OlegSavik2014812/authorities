package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.UserConverter;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.CompositePermissionId;
import com.scnsoft.permissions.persistence.entity.Permission;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.repository.GroupRepository;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User, UserDTO, Long> {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserConverter converter;

    private final PermissionRepository permissionRepository;

    public UserService(UserRepository userRepository,
                       GroupRepository groupRepository,
                       UserConverter converter, PermissionRepository permissionRepository) {
        super(userRepository, converter);
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.converter = converter;
        this.permissionRepository = permissionRepository;
    }

    public UserDTO signIn(UserDTO userDTO) {
        User user = userRepository.findUserByLogin(userDTO.getLogin())
                .filter(user1 -> user1.getPassword()
                        .equals(userDTO.getPassword()))
                .orElse(null);
        return converter.toDTO(user);
    }

    public Optional<UserDTO> signUp(UserDTO userDTO) {
        return Optional.ofNullable(userDTO)
                .filter(userDTO1 -> !userRepository.existsByLogin(userDTO1.getLogin()))
                .map(userDTO1 -> {
                    userDTO1.setPassword(encrypt(userDTO1.getPassword()));
                    saveEntity(userDTO1);
                    return userDTO1;
                });
    }

    public Optional<UserDTO> findByLogin(String name) {
        return userRepository.findUserByLogin(name)
                .map(converter::toDTO);
    }

    public void assignGroup(String login, String userGroupName) {
        userRepository.findUserByLogin(login)
                .ifPresent(user1 -> {
                    groupRepository.findUserGroupByName(userGroupName)
                            .ifPresent(user1::setGroup);
                    userRepository.save(user1);
                });
    }

    public void assignAdditionalPermission(String login, String permissionName, boolean isEnabled) {
        User user = userRepository.findUserByLogin(login).orElseThrow(RuntimeException::new);
        Permission permission = permissionRepository.findPermissionByName(permissionName).orElseThrow(RuntimeException::new);
        AdditionalPermission additionalPermission = build(user, permission, isEnabled);
        user.getAdditionalPermissions().add(additionalPermission);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> findAll() {
        return entities().collect(Collectors.toList());
    }

    private String encrypt(String s) {
        return DigestUtils.md5Hex(s);
    }

    private AdditionalPermission build(User user, Permission permission, boolean isEnabled) {
        CompositePermissionId compositePermissionId = new CompositePermissionId();
        compositePermissionId.setUserId(user.getId());
        compositePermissionId.setPermissionId(permission.getId());
        AdditionalPermission additionalPermission = new AdditionalPermission();
        additionalPermission.setId(compositePermissionId);
        additionalPermission.setUser(user);
        additionalPermission.setPermission(permission);
        additionalPermission.setEnabled(isEnabled);
        return additionalPermission;
    }
}