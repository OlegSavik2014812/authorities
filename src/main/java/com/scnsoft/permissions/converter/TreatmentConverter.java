package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.TreatmentDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Treatment;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TreatmentConverter extends BaseDentalRequestConverter<Treatment, TreatmentDTO> {

    @Autowired
    public TreatmentConverter(UserToothRepository userToothRepository) {
        super(userToothRepository);
    }

    @Override
    public TreatmentDTO toDTO(Treatment entity) {
        TreatmentDTO dto = initDTO(entity, new TreatmentDTO());
        dto.setCost(entity.getCost());
        return dto;

    }

    @Override
    public Treatment toPersistence(TreatmentDTO entity) {
        Treatment treatment = initPersistence(entity, new Treatment());
        treatment.setCost(entity.getCost());
        return treatment;
    }
}
