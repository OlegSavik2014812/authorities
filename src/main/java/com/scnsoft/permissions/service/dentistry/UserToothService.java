package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.ComplaintConverter;
import com.scnsoft.permissions.converter.ConverterUtils;
import com.scnsoft.permissions.converter.TreatmentConverter;
import com.scnsoft.permissions.converter.UserToothConverter;
import com.scnsoft.permissions.dto.dental.BaseDentalRequestDTO;
import com.scnsoft.permissions.dto.dental.ComplaintDTO;
import com.scnsoft.permissions.dto.dental.TreatmentDTO;
import com.scnsoft.permissions.dto.dental.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.LongBinaryOperator;
import java.util.function.UnaryOperator;

@Service
public class UserToothService extends BaseCrudService<UserTooth, UserToothDTO, Long> {
    private final UserToothRepository userToothRepository;
    private final UserToothConverter userToothConverter;
    private final ComplaintConverter complaintConverter;
    private final TreatmentConverter treatmentConverter;
    private final LongBinaryOperator idSupplier;

    @Autowired
    public UserToothService(UserToothRepository userToothRepository,
                            UserToothConverter userToothConverter,
                            ComplaintConverter complaintConverter,
                            TreatmentConverter treatmentConverter) {
        super(userToothRepository, userToothConverter);
        this.userToothRepository = userToothRepository;
        this.userToothConverter = userToothConverter;
        this.complaintConverter = complaintConverter;
        this.treatmentConverter = treatmentConverter;
        idSupplier = (toothNumber, userId) -> Long.parseLong(toothNumber + Strings.EMPTY + userId);
    }

    public List<UserToothDTO> getUserTeeth(Long userId) {
        return Optional.ofNullable(userId)
                .map(userToothRepository::findAllByUserId)
                .map(list -> ConverterUtils.transform(list, userToothConverter::toDTO))
                .orElse(Collections.emptyList());
    }

    @Override
    public UserToothDTO save(UserToothDTO userToothDTO) {
        UserTooth userTooth = Optional.of(userToothDTO)
                .map(this::setId)
                .map(userToothConverter::toPersistence)
                .map(userToothRepository::save)
                .orElseThrow(() -> new UnsupportedOperationException("Unable to save user tooth"));

        Long id = userTooth.getId();

        UnaryOperator<ComplaintDTO> complaintUserToothIdSetter = getUserToothIdSetter(id);
        UnaryOperator<TreatmentDTO> treatmentUserToothIdSetter = getUserToothIdSetter(id);

        userTooth.setComplaints(ConverterUtils.transform(userToothDTO.getComplaints(),
                complaintUserToothIdSetter.andThen(complaintConverter::toPersistence)));

        userTooth.setTreatments(ConverterUtils.transform(userToothDTO.getTreatments(),
                treatmentUserToothIdSetter.andThen(treatmentConverter::toPersistence)));

        UserTooth complainedTooth = userToothRepository.save(userTooth);
        return userToothConverter.toDTO(complainedTooth);
    }

    private UserToothDTO setId(UserToothDTO userToothDTO) {
        Long id = Optional.ofNullable(userToothDTO.getId())
                .filter(id1 -> id1 > 10L)
                .orElse(idSupplier.applyAsLong(userToothDTO.getToothNumber(), userToothDTO.getUserId()));
        if (id < 11L) {
            throw new UnsupportedOperationException("Unable to save user tooth, invalid id");
        }
        userToothDTO.setId(id);
        return userToothDTO;
    }

    private <K extends BaseDentalRequestDTO> UnaryOperator<K> getUserToothIdSetter(Long userToothId) {
        return k -> {
            k.setUserToothId(userToothId);
            return k;
        };
    }
}