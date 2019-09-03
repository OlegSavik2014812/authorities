package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.dental.TreatmentDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Treatment;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TreatmentConverter extends BaseDentalRequestConverter<Treatment, TreatmentDTO> {
    private final UserToothRepository userToothRepository;

    @Override
    public TreatmentDTO toDTO(Treatment entity) {
        TreatmentDTO dto = initDTO(entity, new TreatmentDTO());
        dto.setPrice(entity.getPrice());
        return dto;
    }

    @Override
    public Treatment toPersistence(TreatmentDTO entityDTO) {
        UserTooth tooth = userToothRepository.findById(entityDTO.getUserToothId()).orElseGet(UserTooth::new);
        LocalDateTime date = Optional.ofNullable(entityDTO.getDate()).orElseGet(LocalDateTime::now);
        String operation = entityDTO.getDescription();
        BigDecimal price = entityDTO.getPrice();

        return Treatment.treat()
                .describe(operation)
                .what(tooth)
                .calculate(price)
                .when(date)
                .build();
    }
}