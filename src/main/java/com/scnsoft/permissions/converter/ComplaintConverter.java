package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.dental.ComplaintDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Complaint;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ComplaintConverter extends BaseDentalRequestConverter<Complaint, ComplaintDTO> {
    private UserToothRepository userToothRepository;

    @Autowired
    public ComplaintConverter(UserToothRepository userToothRepository) {
        this.userToothRepository = userToothRepository;
    }

    @Override
    public ComplaintDTO toDTO(Complaint entity) {
        return initDTO(entity, new ComplaintDTO());
    }

    @Override
    public Complaint toPersistence(ComplaintDTO entity) {
        UserTooth tooth = userToothRepository.findById(entity.getUserToothId()).orElseGet(UserTooth::new);
        LocalDateTime date = Optional.ofNullable(entity.getDate()).orElseGet(LocalDateTime::now);
        String problem = entity.getDescription();

        return Complaint.complain()
                .about(tooth)
                .describe(problem)
                .when(date)
                .build();
    }
}