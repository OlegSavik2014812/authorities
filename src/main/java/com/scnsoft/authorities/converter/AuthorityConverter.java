package com.scnsoft.authorities.converter;

import com.scnsoft.authorities.dto.AuthorityDTO;
import com.scnsoft.authorities.persistence.entity.Authority;
import org.springframework.stereotype.Component;

@Component
public class AuthorityConverter implements EntityConverter<Authority, AuthorityDTO> {
    @Override
    public AuthorityDTO toDTO(Authority entity) {

        return null;
    }

    @Override
    public Authority toPersistence(AuthorityDTO entity) {
        return null;
    }
}
