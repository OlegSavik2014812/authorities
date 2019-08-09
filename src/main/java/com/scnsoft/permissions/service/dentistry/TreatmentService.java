package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.TreatmentConverter;
import com.scnsoft.permissions.dto.TreatmentDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Treatment;
import com.scnsoft.permissions.persistence.repository.dentistry.TreatmentRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TreatmentService extends BaseCrudService<Treatment, TreatmentDTO, Long> {

    @Autowired
    public TreatmentService(TreatmentRepository repository, TreatmentConverter converter) {
        super(repository, converter);
    }

    @Override
    public Collection<TreatmentDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}
