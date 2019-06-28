package com.scnsoft.authorities.converter;

import com.scnsoft.authorities.dto.UserGroupDTO;
import com.scnsoft.authorities.persistence.entity.Authority;
import com.scnsoft.authorities.persistence.entity.User;
import com.scnsoft.authorities.persistence.entity.UserGroup;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserGroupConverter implements EntityConverter<UserGroup, UserGroupDTO> {
    @Override
    public UserGroupDTO toDTO(UserGroup entity) {
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId(entity.getId());
        userGroupDTO.setName(entity.getName());
        userGroupDTO.setAuthoritiesIds(entity.getAuthorities()
                .stream()
                .map(Authority::getId)
                .collect(Collectors.toList()));
        userGroupDTO.setUserIds(entity.getUsers()
                .stream()
                .map(User::getId)
                .collect(Collectors.toList()));
        return userGroupDTO;
    }

    @Override
    public UserGroup toPersistence(UserGroupDTO entity) {
        return null;
    }
}
