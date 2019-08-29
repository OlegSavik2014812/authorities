package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.TreatmentConverter;
import com.scnsoft.permissions.dto.dental.TreatmentDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Treatment;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.TreatmentRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.math.BigDecimal.ZERO;
import static java.time.LocalDateTime.now;

@Service
public class TreatmentService extends DentalRequestService<Treatment, TreatmentDTO> {
    public TreatmentService(TreatmentRepository treatmentRepository, TreatmentConverter converter, UserToothRepository userToothRepository) {
        super(treatmentRepository, converter, userToothRepository);
    }

    public void treat(TreatmentDTO treatmentDTO) {
        BigDecimal price = Optional.ofNullable(treatmentDTO.getPrice()).orElse(ZERO);
        BiFunction<UserTooth, String, Treatment> converter =
                (tooth, procedure) ->
                        Treatment.treat()
                                .what(tooth)
                                .when(now())
                                .describe(procedure)
                                .calculate(price)
                                .build();
        save(treatmentDTO, converter);
    }
}