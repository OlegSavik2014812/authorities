package com.scnsoft.authorities.converter;

import com.scnsoft.authorities.dto.EntityDTO;
import com.scnsoft.authorities.persistence.entity.PersistenceEntity;

public interface EntityConverter<T extends PersistenceEntity, K extends EntityDTO> {
    K toDTO(T entity);

    T toPersistence(K entity);
}
