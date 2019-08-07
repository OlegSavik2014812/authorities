package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.TreatmentDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Treatment;
import com.scnsoft.permissions.persistence.repository.dentistry.TreatmentRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TreatmentConverter implements EntityConverter<Treatment, TreatmentDTO> {
    private final TreatmentRepository treatmentRepository;
    @Autowired
    private UserToothRepository userToothRepository;

    @Autowired
    public TreatmentConverter(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public TreatmentDTO toDTO(Treatment entity) {
        return TreatmentDTO.builder()
                .cost(entity.getCost())
                .date(entity.getDate())
                .description(entity.getDescription())
                .userToothId(entity.getUserTooth().getId())
                .id(entity.getId())
                .build();
    }

    @Override
    public Treatment toPersistence(TreatmentDTO entity) {
        if (entity == null) {
            return null;
        }
        return treatmentRepository.findById(entity.getId())
                .orElseGet(() -> {
                            Treatment treatment = new Treatment();
                            treatment.setCost(entity.getCost());
                            treatment.setDate(entity.getDate());
                            treatment.setDescription(entity.getDescription());
                            Optional.of(entity.getUserToothId()).flatMap(userToothRepository::findById).ifPresent(treatment::setUserTooth);
                            Optional.ofNullable(entity.getId()).ifPresent(treatment::setId);
                            return treatment;
                        }
                );
    }
}
