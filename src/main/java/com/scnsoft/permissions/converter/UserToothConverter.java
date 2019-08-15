package com.scnsoft.permissions.converter;

import com.google.common.collect.Lists;
import com.scnsoft.permissions.dto.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.ComplaintRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.TreatmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class UserToothConverter implements EntityConverter<UserTooth, UserToothDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserToothConverter.class);
    private final TreatmentConverter treatmentConverter;
    private final TreatmentRepository treatmentRepository;
    private final ComplaintConverter complaintConverter;
    private final ComplaintRepository complaintRepository;
    private final ToothRepository toothRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserToothConverter(ComplaintConverter complaintConverter,
                              TreatmentConverter treatmentConverter,
                              ToothRepository toothRepository,
                              TreatmentRepository treatmentRepository,
                              ComplaintRepository complaintRepository,
                              UserRepository userRepository) {
        this.complaintConverter = complaintConverter;
        this.treatmentConverter = treatmentConverter;
        this.toothRepository = toothRepository;
        this.treatmentRepository = treatmentRepository;
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
    }

    @Override
    public UserToothDTO toDTO(UserTooth entity) {
        Tooth tooth = entity.getTooth();

        UserToothDTO.UserToothDTOBuilder userToothDTOBuilder = UserToothDTO.builder()
                .id(entity.getId())
                .toothNumber(tooth.getId())
                .toothType(tooth.getType().toString())
                .userId(entity.getUser().getId());

        setDentalReq(entity::getComplaints, complaintConverter::toDTO, userToothDTOBuilder::complaints);
        setDentalReq(entity::getTreatments, treatmentConverter::toDTO, userToothDTOBuilder::treatments);

        return userToothDTOBuilder.build();
    }

    @Override
    public UserTooth toPersistence(UserToothDTO entity) {
        UserTooth userTooth = new UserTooth();
        Long toothNumber = entity.getToothNumber();
        Long userId = entity.getUserId();
        toothRepository.findById(toothNumber).ifPresent(userTooth::setTooth);
        userRepository.findById(userId).ifPresent(userTooth::setUser);
        LongSupplier longSupplier = () -> Long.parseLong(toothNumber + "" + userId);
        Long id = Optional.ofNullable(entity.getId()).orElseGet(longSupplier::getAsLong);
        userTooth.setId(id);

        setDentalReq(entity::getComplaints, complaintConverter::toPersistence, userTooth::setComplaints);
        setDentalReq(entity::getTreatments, treatmentConverter::toPersistence, userTooth::setTreatments);
        return userTooth;
    }

    private <T, K> void setDentalReq(Supplier<Iterable<T>> supplier,
                                     Function<T, K> mapper,
                                     Consumer<List<K>> consumer) {

        Iterable<T> ts = Optional.ofNullable(supplier.get()).orElse(Collections.emptyList());
        List<T> suppliedList = Lists.newArrayList(ts);
        if (suppliedList.isEmpty()) {
            return;
        }
        suppliedList.stream()
                .filter(Objects::nonNull)
                .map(mapper)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                .ifPresent(consumer);
    }
}