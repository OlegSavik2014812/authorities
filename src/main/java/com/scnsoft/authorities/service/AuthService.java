package com.scnsoft.authorities.service;

import com.scnsoft.authorities.converter.AuthorityConverter;
import com.scnsoft.authorities.dto.AuthorityDTO;
import com.scnsoft.authorities.persistence.entity.Authority;
import com.scnsoft.authorities.persistence.entity.UserGroup;
import com.scnsoft.authorities.persistence.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService extends BaseService<Authority, AuthorityDTO, Long> {
    private final AuthorityRepository authorityRepository;
    private final AuthorityConverter authorityConverter;

    public AuthService(AuthorityRepository authorityRepository, AuthorityConverter authorityConverter) {
        super(authorityRepository, authorityConverter);
        this.authorityRepository = authorityRepository;
        this.authorityConverter = authorityConverter;
    }

    public AuthorityDTO findByName(String name) {
        return authorityRepository.findAuthorityByName(name)
                .map(authorityConverter::toDTO)
                .orElseThrow(NullPointerException::new);
    }

    public void addUserGroups(Authority authority, UserGroup userGroup) {
        authorityRepository
                .findAuthorityByName(authority.getName())
                .ifPresent(authority1 -> {
                    authority1.getUserGroups().add(userGroup);
                    authorityRepository.save(authority1);
                });
    }
@Override
    public List<AuthorityDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}
