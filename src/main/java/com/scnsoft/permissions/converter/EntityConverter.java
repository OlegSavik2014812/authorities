package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.EntityDTO;
import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import com.scnsoft.permissions.util.ExecutionTime;

public interface EntityConverter<T extends PersistenceEntity, K extends EntityDTO> {
    @ExecutionTime
    K toDTO(T entity);

    @ExecutionTime
    T toPersistence(K entity);
}