package com.scnsoft.authorities.service;

import com.scnsoft.authorities.converter.UserConverter;
import com.scnsoft.authorities.dto.UserDTO;
import com.scnsoft.authorities.persistence.entity.User;
import com.scnsoft.authorities.persistence.repository.UserGroupRepository;
import com.scnsoft.authorities.persistence.repository.UserRepository;
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

    public UserService(UserRepository userRepository,
                       UserGroupRepository userGroupRepository,
                       UserConverter converter) {
        super(userRepository, converter);
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.converter = converter;
    }

    public UserDTO signIn(UserDTO userDTO) {
        User user = userRepository.findUserByLogin(userDTO.getLogin())
                .filter(user1 -> {
                    String encrypted = DigestUtils.md5Hex(userDTO.getPassword());
                    return user1.getPassword()
                            .equals(encrypted);
                })
                .orElse(null);
        return converter.toDTO(user);
    }

    public UserDTO signUp(UserDTO userDTO) {
        return Optional.ofNullable(userDTO)
                .filter(userDTO1 -> !userRepository.existsByLogin(userDTO1.getLogin()))
                .map(userDTO1 -> {
                    userDTO1.setPassword(encrypt(userDTO1.getPassword()));
                    saveEntity(userDTO1);
                    return userDTO1;
                }).orElseThrow(RuntimeException::new);
    }

    public Optional<UserDTO> findByLogin(String name) {
        return userRepository.findUserByLogin(name).map(converter::toDTO);
    }

    public void assignGroup(String login, String userGroupName) {
        userRepository.findUserByLogin(login)
                .ifPresent(user1 -> {
                    userGroupRepository.findUserGroupByName(userGroupName)
                            .ifPresent(user1::setGroup);
                    userRepository.save(user1);
                });
    }

    @Override
    public List<UserDTO> findAll() {
        return entities().collect(Collectors.toList());
    }

    private String encrypt(String s) {
        return DigestUtils.md5Hex(s);
    }
}


