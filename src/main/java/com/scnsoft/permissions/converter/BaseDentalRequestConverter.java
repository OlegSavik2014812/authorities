package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.BaseDentalRequestDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.BaseDentalRequest;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;

import java.util.Optional;

public abstract class BaseDentalRequestConverter<T extends BaseDentalRequest, K extends BaseDentalRequestDTO>
        implements EntityConverter<T, K> {

    K initDTO(T persistence, K dto) {
        Optional.ofNullable(persistence.getId())
                .ifPresent(dto::setId);

        Optional.ofNullable(persistence.getUserTooth())
                .map(UserTooth::getId)
                .ifPresent(dto::setUserToothId);

        dto.setDescription(persistence.getDescription());
        dto.setDate(persistence.getDate());
        return dto;
    }
}