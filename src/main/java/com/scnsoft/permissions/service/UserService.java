package com.scnsoft.permissions.service;

import com.scnsoft.permissions.converter.UserConverter;
import com.scnsoft.permissions.dto.UserDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Service
public class UserService extends BaseCrudService<User, UserDTO, Long> {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserConverter userConverter;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository,
                       GroupRepository groupRepository,
                       UserConverter userConverter,
                       BCryptPasswordEncoder encoder) {
        super(userRepository, userConverter);
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
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
                .map(super::save)
                .orElseThrow(() -> new NullPointerException("Unable to save user"));
    }

    private String encrypt(String string) {
        if (Strings.isBlank(string)) {
            throw new NullPointerException("Password is empty");
        }
        return encoder.encode(string);
    }

    public boolean existByLogin(String userLogin) {
        return userRepository.existsByLogin(userLogin);
    }

    public Optional<UserDTO> findByLogin(String name) {
        return userRepository.findUserByLogin(name)
                .map(userConverter::toDTO);
    }

    public UserDTO updateGroup(Long userId, Long groupId) {
        UnaryOperator<User> updateGroup = user1 -> {
            Group group = Optional.ofNullable(groupId)
                    .flatMap(groupRepository::findById)
                    .orElse(null);
            user1.setGroup(group);
            return user1;
        };
        return executeAssigment(updateGroup, userId);
    }

    public Page<UserDTO> findWithPermission(int page, int size, String permission) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Long> userIdsByPermissions = userRepository.findUserIdsByPermissions(List.of(permission), pageable);
        List<User> users = userRepository.findAllById(userIdsByPermissions.getContent());
        return new PageImpl<>(users, pageable, userIdsByPermissions.getTotalElements()).map(userConverter::toDTO);
    }

    public Page<UserDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> all = userRepository.findAll(pageable);
        return all.map(userConverter::toDTO);
    }

    private UserDTO executeAssigment(UnaryOperator<User> assignAction, Long userId) {
        User user = getById(userRepository, userId);
        User assignedUser = assignAction.apply(user);
        User savedUser = userRepository.save(assignedUser);
        return userConverter.toDTO(savedUser);
    }

    private <T> T getById(CrudRepository<T, Long> repository, Long id) {
        return repository.findById(id).orElseThrow(() -> new NullPointerException("No such entity"));
    }
}