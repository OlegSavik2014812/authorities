package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.ComplaintConverter;
import com.scnsoft.permissions.dto.ComplaintDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Complaint;
import com.scnsoft.permissions.persistence.repository.dentistry.ComplaintRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ComplaintService extends BaseCrudService<Complaint, ComplaintDTO, Long> {

    public ComplaintService(ComplaintRepository repository, ComplaintConverter converter) {
        super(repository, converter);
    }

    @Override
    public Collection<ComplaintDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}
