package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.ComplaintDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Complaint;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComplaintConverter extends BaseDentalRequestConverter<Complaint, ComplaintDTO> {
    @Autowired
    public ComplaintConverter(UserToothRepository userToothRepository) {
        super(userToothRepository);
    }

    @Override
    public ComplaintDTO toDTO(Complaint entity) {
        return initDTO(entity, new ComplaintDTO());
    }

    @Override
    public Complaint toPersistence(ComplaintDTO entity) {
        return initPersistence(entity, new Complaint());
    }
}