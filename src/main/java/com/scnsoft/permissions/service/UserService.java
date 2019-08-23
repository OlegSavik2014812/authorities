package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.UserConverter;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.CompositePermissionId;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.entity.permission.Permission;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import com.scnsoft.permissions.persistence.repository.permission.PermissionRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends BaseCrudService<User, UserDTO, Long> {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserConverter userConverter;
    private final PermissionRepository permissionRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository,
                       GroupRepository groupRepository,
                       PermissionRepository permissionRepository,
                       UserConverter userConverter,
                       BCryptPasswordEncoder encoder) {
        super(userRepository, userConverter);
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.permissionRepository = permissionRepository;
        this.userConverter = userConverter;
        this.encoder = encoder;
    }

    public UserDTO save(UserDTO userDTO) {
        return Optional.ofNullable(userDTO)
                .map(userDTO1 -> {
                    String password = userDTO1.getPassword();
                    userDTO1.setPassword(encrypt(password));
                    return userDTO1;
                })
                .map(userConverter::toPersistence)
                .map(userRepository::save)
                .map(userConverter::toDTO)
                .orElseThrow(() -> new NullPointerException("Unable to save user"));
    }
    private String encrypt(String string) {
        return Optional.ofNullable(string)
                .filter(Strings::isNotBlank)
                .map(encoder::encode)
                .orElseThrow(() -> new NullPointerException("Password is empty"));
    }

    public Optional<UserDTO> findByLogin(String name) {
        return userRepository.findUserByLogin(name)
                .map(userConverter::toDTO);
    }

    public UserDTO assignGroup(Long userId, Long groupId) {
        User user = getById(userRepository, userId);
        Group group = getById(groupRepository, groupId);
        user.setGroup(group);
        User save = userRepository.save(user);
        return userConverter.toDTO(save);
    }

    public UserDTO assignAdditionalPermission(Long userId, Long permissionId, boolean isEnabled) {
        User user = getById(userRepository, userId);
        Permission permission = getById(permissionRepository, permissionId);
        user.getAdditionalPermissions().add(
                AdditionalPermission.builder()
                        .id(new CompositePermissionId(userId, permissionId))
                        .user(user)
                        .permission(permission)
                        .isEnabled(isEnabled)
                        .build()
        );
        User save = userRepository.save(user);
        return userConverter.toDTO(save);
    }

    private <T> T getById(CrudRepository<T, Long> repository, Long id) {
        return repository.findById(id).orElseThrow(() -> new NullPointerException("No such entity"));
    }
}