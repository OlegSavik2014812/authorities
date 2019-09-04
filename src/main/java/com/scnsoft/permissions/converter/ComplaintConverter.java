package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.dental.ComplaintDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Complaint;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ComplaintConverter extends BaseDentalRequestConverter<Complaint, ComplaintDTO> {
    private final UserToothRepository userToothRepository;

    @Override
    public ComplaintDTO toDTO(Complaint entity) {
        return initDTO(entity, new ComplaintDTO());
    }

    @Override
    public Complaint toPersistence(ComplaintDTO entityDTO) {
        UserTooth tooth = userToothRepository.findById(entityDTO.getUserToothId())
                .orElseThrow(NullPointerException::new);
        LocalDateTime date = Optional.ofNullable(entityDTO.getDate())
                .orElseGet(LocalDateTime::now);
        String problem = entityDTO.getDescription();

        return Complaint.complain()
                .about(tooth)
                .describe(problem)
                .when(date)
                .build();
    }
}