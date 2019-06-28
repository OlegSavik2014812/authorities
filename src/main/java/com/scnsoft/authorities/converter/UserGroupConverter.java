package com.scnsoft.authorities.converter;

import com.scnsoft.authorities.dto.UserGroupDTO;
import com.scnsoft.authorities.persistence.entity.Authority;
import com.scnsoft.authorities.persistence.entity.User;
import com.scnsoft.authorities.persistence.entity.UserGroup;
import com.scnsoft.authorities.persistence.repository.AuthorityRepository;
import com.scnsoft.authorities.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserGroupConverter implements EntityConverter<UserGroup, UserGroupDTO> {
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    public UserGroupConverter(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserGroupDTO toDTO(UserGroup entity) {
        if (entity == null) {
            return null;
        }
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
        if (entity == null) {
            return null;
        }
        UserGroup userGroup = new UserGroup();
        Optional.ofNullable(entity.getId())
                .ifPresent(userGroup::setId);

        userGroup.setName(entity.getName());

        List<Authority> authorities = new ArrayList<>();
        authorityRepository.findAll().forEach(authorities::add);

        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);

        userGroup.setAuthorities(authorities);
        userGroup.setUsers(users);

        return userGroup;
    }
}
