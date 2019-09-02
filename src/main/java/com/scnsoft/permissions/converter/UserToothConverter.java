package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.dental.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserToothConverter implements EntityConverter<UserTooth, UserToothDTO> {
    private final TreatmentConverter treatmentConverter;
    private final ComplaintConverter complaintConverter;
    private final ToothRepository toothRepository;
    private final UserRepository userRepository;

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
        return userTooth;
    }

    private <T, K> void setDentalReq(Supplier<List<T>> supplier, Function<T, K> mapper, Consumer<List<K>> consumer) {
        List<T> entities = supplier.get();
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        entities.stream()
                .filter(Objects::nonNull)
                .map(mapper)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                .ifPresent(consumer);
    }
}