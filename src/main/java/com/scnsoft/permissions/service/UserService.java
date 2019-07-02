package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.PermissionConverter;
import com.scnsoft.permissions.converter.UserConverter;
import com.scnsoft.permissions.dto.PermissionDTO;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.Permission;
import com.scnsoft.permissions.persistence.entity.CompositePermissionId;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.repository.PermissionRepository;
import com.scnsoft.permissions.persistence.repository.UserGroupRepository;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User, UserDTO, Long> {
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserConverter converter;
    private final PermissionConverter permissionConverter;

    private final PermissionRepository permissionRepository;

    public UserService(UserRepository userRepository,
                       UserGroupRepository userGroupRepository,
                       UserConverter converter, PermissionConverter permissionConverter, PermissionRepository permissionRepository) {
        super(userRepository, converter);
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.converter = converter;
        this.permissionConverter = permissionConverter;
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
                    userGroupRepository.findUserGroupByName(userGroupName)
                            .ifPresent(user1::setGroup);
                    userRepository.save(user1);
                });
    }

    public void assignAdditionalPermission(UserDTO userDTO, PermissionDTO permissionDTO, boolean isEnabled) {
        userRepository.findById(userDTO.getId())
                .filter(user -> permissionRepository.existsById(permissionDTO.getId()))
                .map(user -> {
                    CompositePermissionId compositePermissionId = new CompositePermissionId(user.getId(), permissionDTO.getId());
                    Permission permission = permissionConverter.toPersistence(permissionDTO);
                    AdditionalPermission additionalPermission = AdditionalPermission.builder()
                            .id(compositePermissionId)
                            .permission(permission)
                            .user(user)
                            .isEnabled(isEnabled)
                            .build();

                    user.getAdditionalPermissions()
                            .add(additionalPermission);
                    return user;
                }).ifPresent(userRepository::save);
    }

    @Override
    public List<UserDTO> findAll() {
        return entities().collect(Collectors.toList());
    }

    private String encrypt(String s) {
        return DigestUtils.md5Hex(s);
    }
}