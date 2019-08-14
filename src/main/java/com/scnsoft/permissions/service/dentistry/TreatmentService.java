package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.TreatmentConverter;
import com.scnsoft.permissions.dto.TreatmentDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Treatment;
import com.scnsoft.permissions.persistence.repository.dentistry.TreatmentRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TreatmentService extends BaseCrudService<Treatment, TreatmentDTO, Long> {

    private final UserToothRepository userToothRepository;
    private final TreatmentRepository treatmentRepository;

    @Autowired
    public TreatmentService(TreatmentRepository repository, TreatmentConverter converter, UserToothRepository userToothRepository) {
        super(repository, converter);
        this.treatmentRepository = repository;
        this.userToothRepository = userToothRepository;
    }

    public void treat(Long userTootId, TreatmentDTO treatmentDTO) {
        userToothRepository.findById(userTootId)
                .ifPresent(userTooth -> {
                            LocalDate date = Optional.ofNullable(treatmentDTO.getDate())
                                    .orElseGet(LocalDate::now);
                            String operationDescription = treatmentDTO.getDescription();
                            BigDecimal cost = treatmentDTO.getCost();
                            treatmentRepository.save(
                                    Treatment.treat()
                                            .what(userTooth)
                                            .when(date)
                                            .describe(operationDescription)
                                            .estimate(cost)
                                            .build());
                        }
                );
    }

    @Override
    public Collection<TreatmentDTO> findAll() {
        return entities().collect(Collectors.toList());
    }
}
