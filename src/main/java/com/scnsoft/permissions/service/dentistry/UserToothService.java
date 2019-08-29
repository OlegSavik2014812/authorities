package com.scnsoft.permissions.service.dentistry;

import com.scnsoft.permissions.converter.ComplaintConverter;
import com.scnsoft.permissions.converter.TreatmentConverter;
import com.scnsoft.permissions.converter.UserToothConverter;
import com.scnsoft.permissions.dto.dental.BaseDentalRequestDTO;
import com.scnsoft.permissions.dto.dental.ComplaintDTO;
import com.scnsoft.permissions.dto.dental.TreatmentDTO;
import com.scnsoft.permissions.dto.dental.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.BaseDentalRequest;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserToothService extends BaseCrudService<UserTooth, UserToothDTO, Long> {
    private final UserToothRepository userToothRepository;
    private final UserToothConverter userToothConverter;
    private final ComplaintConverter complaintConverter;
    private final TreatmentConverter treatmentConverter;

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
    }

    public List<UserToothDTO> getUserTeeth(Long userId) {
        return Optional.ofNullable(userId)
                .map(userToothRepository::findAllByUserId)
                .map(iterable ->
                        StreamSupport.stream(iterable.spliterator(), false)
                                .map(userToothConverter::toDTO)
                                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public Long save(UserToothDTO userToothDTO) {
        if (Objects.isNull(userToothDTO)) {
            return 0L;
        }
        UserTooth userTooth = Optional.of(userToothDTO)
                .map(userToothConverter::toPersistence)
                .map(userToothRepository::save)
                .orElseThrow(() -> new UnsupportedOperationException("Unable to save user tooth"));

        Long id = userTooth.getId();

        UnaryOperator<ComplaintDTO> complaintUserToothIdSetter = getUserToothIdSetter(id);
        UnaryOperator<TreatmentDTO> treatmentUserToothIdSetter = getUserToothIdSetter(id);

        convert(userToothDTO::getComplaints, complaintUserToothIdSetter.andThen(complaintConverter::toPersistence), userTooth::setComplaints);
        convert(userToothDTO::getTreatments, treatmentUserToothIdSetter.andThen(treatmentConverter::toPersistence), userTooth::setTreatments);

        UserTooth complainedTooth = userToothRepository.save(userTooth);
        return complainedTooth.getId();
    }

    private <K extends BaseDentalRequestDTO> UnaryOperator<K> getUserToothIdSetter(Long userToothId) {
        return k -> {
            k.setUserToothId(userToothId);
            return k;
        };
    }

    private <T extends BaseDentalRequest, K extends BaseDentalRequestDTO> void convert(Supplier<List<K>> supplier, Function<K, T> mapper, Consumer<List<T>> consumer) {
        List<K> dtoItems = supplier.get();
        if (CollectionUtils.isNotEmpty(dtoItems)) {
            dtoItems.stream()
                    .map(mapper)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                    .ifPresent(consumer);
        }
    }
}