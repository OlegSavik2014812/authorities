package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.BaseDentalRequestDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.BaseDentalRequest;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;

import java.time.LocalDate;
import java.util.Optional;

public abstract class BaseDentalRequestConverter<T extends BaseDentalRequest, K extends BaseDentalRequestDTO>
        implements EntityConverter<T, K> {

    private final UserToothRepository userToothRepository;

    public BaseDentalRequestConverter(UserToothRepository userToothRepository) {
        this.userToothRepository = userToothRepository;
    }

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

    T initPersistence(K dto, T persistence) {

        Optional.ofNullable(dto.getId())
                .ifPresent(persistence::setId);
        Optional.ofNullable(dto.getDate())
                .ifPresentOrElse(persistence::setDate, () -> persistence.setDate(LocalDate.now()));
        userToothRepository.findById(dto.getUserToothId())
                .ifPresent(persistence::setUserTooth);
        persistence.setDescription(dto.getDescription());
        return persistence;
    }
}