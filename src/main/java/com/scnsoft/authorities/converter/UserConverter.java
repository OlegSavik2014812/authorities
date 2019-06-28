package com.scnsoft.authorities.converter;

import com.scnsoft.authorities.dto.UserDTO;
import com.scnsoft.authorities.persistence.entity.User;
import com.scnsoft.authorities.persistence.entity.UserGroup;
import com.scnsoft.authorities.persistence.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {
    @Autowired
    private UserGroupRepository repository;

    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        Optional.ofNullable(entity.getId())
                .ifPresent(userDTO::setId);
        userDTO.setId(entity.getId());
        userDTO.setLogin(entity.getLogin());
        userDTO.setPassword(entity.getPassword());

        Optional.ofNullable(entity.getGroup())
                .map(UserGroup::getId)
                .ifPresent(userDTO::setGroupId);

        return userDTO;
    }

    @Override
    public User toPersistence(UserDTO entity) {
        if (entity == null) {
            return null;
        }
        User user = new User();
        Optional.ofNullable(entity.getId())
                .ifPresent(user::setId);
        user.setLogin(entity.getLogin());
        user.setPassword(entity.getPassword());
        Optional.ofNullable(entity.getGroupId())
                .flatMap(repository::findById)
                .ifPresent(user::setGroup);
        return user;
    }
}
