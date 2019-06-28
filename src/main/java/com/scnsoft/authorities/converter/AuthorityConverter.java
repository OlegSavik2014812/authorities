package com.scnsoft.authorities.converter;

import com.scnsoft.authorities.dto.AuthorityDTO;
import com.scnsoft.authorities.persistence.entity.Authority;
import com.scnsoft.authorities.persistence.entity.UserGroup;
import com.scnsoft.authorities.persistence.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AuthorityConverter implements EntityConverter<Authority, AuthorityDTO> {
    @Autowired
    private UserGroupRepository userGroupRepository;

    @Override
    public AuthorityDTO toDTO(Authority entity) {
        if (entity == null) {
            return null;
        }
        AuthorityDTO authorityDTO = new AuthorityDTO();
        Optional.ofNullable(entity.getId()).ifPresent(authorityDTO::setId);
        authorityDTO.setName(entity.getName());

        List<Long> groupIds = entity.getUserGroups().stream()
                .map(UserGroup::getId)
                .collect(Collectors.toList());

        authorityDTO.setGroupIds(groupIds);
        return authorityDTO;
    }

    @Override
    public Authority toPersistence(AuthorityDTO entity) {
        if (entity == null) {
            return null;
        }
        Authority authority = new Authority();
        Optional.ofNullable(entity.getId()).ifPresent(authority::setId);
        authority.setName(entity.getName());
        List<UserGroup> list = new ArrayList<>();
        userGroupRepository.findAll().forEach(list::add);

        authority.setUserGroups(list);
        return authority;
    }
}
