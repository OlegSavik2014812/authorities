package com.scnsoft.permissions.converter;

import com.google.common.collect.Lists;
import com.scnsoft.permissions.dto.BaseDentalRequestDTO;
import com.scnsoft.permissions.dto.ComplaintDTO;
import com.scnsoft.permissions.dto.TreatmentDTO;
import com.scnsoft.permissions.dto.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.BaseDentalRequest;
import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.ComplaintRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class UserToothConverter implements EntityConverter<UserTooth, UserToothDTO> {
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
        List<TreatmentDTO> treatments = convert(entity::getTreatments, treatmentConverter::toDTO);
        List<ComplaintDTO> complaints = convert(entity::getComplaints, complaintConverter::toDTO);

        Tooth tooth = entity.getTooth();

        return UserToothDTO.builder()
                .id(entity.getId())
                .toothNumber(tooth.getId())
                .toothType(tooth.getType().toString())
                .treatments(treatments)
                .complaints(complaints)
                .userId(entity.getUser().getId())
                .build();
    }

    @Override
    public UserTooth toPersistence(UserToothDTO entity) {
        if (entity == null) return null;
        UserTooth userTooth = new UserTooth();
        Optional<Long> optionalId = Optional.ofNullable(entity.getId());

        toothRepository.findById(entity.getToothNumber())
                .ifPresent(userTooth::setTooth);

        optionalId.ifPresent(id -> {
            userTooth.setId(id);
            setUpList(() -> treatmentRepository.findAllByUserToothId(id), userTooth::setTreatments);
            setUpList(() -> complaintRepository.findAllByUserToothId(id), userTooth::setComplaints);
        });
        optionalId.flatMap(userRepository::findById).ifPresent(userTooth::setUser);
        return userTooth;
    }

    private <T extends BaseDentalRequestDTO, K extends BaseDentalRequest> List<T> convert(Supplier<List<K>> supplier, Function<K, T> converter) {
        return supplier.get()
                .stream()
                .map(converter)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    private <T> void setUpList(Supplier<Iterable<T>> supplier, Consumer<List<T>> consumer) {
        Iterable<T> elements = supplier.get();
        consumer.accept(Lists.newArrayList(elements));
    }
}