package com.scnsoft.authorities.service;

import com.scnsoft.authorities.converter.UserGroupConverter;
import com.scnsoft.authorities.dto.UserGroupDTO;
import com.scnsoft.authorities.persistence.entity.UserGroup;
import com.scnsoft.authorities.persistence.repository.AuthorityRepository;
import com.scnsoft.authorities.persistence.repository.UserGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserGroupService extends BaseService<UserGroup, UserGroupDTO, Long> {
    private final UserGroupRepository userGroupRepository;
    private final AuthorityRepository authorityRepository;
    private final UserGroupConverter groupConverter;

    public UserGroupService(UserGroupRepository userGroupRepository,
                            AuthorityRepository authorityRepository,
                            UserGroupConverter groupConverter) {
        super(userGroupRepository, groupConverter);
        this.userGroupRepository = userGroupRepository;
        this.authorityRepository = authorityRepository;
        this.groupConverter = groupConverter;
    }

    public Optional<UserGroupDTO> getByName(String userGroupName) {
        return userGroupRepository.findUserGroupByName(userGroupName).map(groupConverter::toDTO);
    }

    public Optional<UserGroupDTO> assignAuthority(String userGroupName, String authorityName) {
        return userGroupRepository.findUserGroupByName(userGroupName)
                .map(userGroup1 -> {
                    authorityRepository.findAuthorityByName(authorityName)
                            .ifPresent(authority -> userGroup1.getAuthorities().add(authority));
                    return userGroupRepository.save(userGroup1);
                }).map(groupConverter::toDTO);
    }

    @Override
    public List<UserGroupDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}
