package com.scnsoft.permissions.converter;

import com.google.common.collect.Lists;
import com.scnsoft.permissions.dto.TreatmentDTO;
import com.scnsoft.permissions.dto.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.TreatmentRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.UserToothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserToothConverter implements EntityConverter<UserTooth, UserToothDTO> {
    private final TreatmentConverter treatmentConverter;
    private final UserToothRepository userToothRepository;
    private final ToothRepository toothRepository;
    private final TreatmentRepository treatmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserToothConverter(TreatmentConverter treatmentConverter, UserToothRepository userToothRepository, ToothRepository toothRepository, TreatmentRepository treatmentRepository, UserRepository userRepository) {
        this.treatmentConverter = treatmentConverter;
        this.userToothRepository = userToothRepository;
        this.toothRepository = toothRepository;
        this.treatmentRepository = treatmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserToothDTO toDTO(UserTooth entity) {
        List<TreatmentDTO> list = entity.getList()
                .stream()
                .map(treatmentConverter::toDTO)
                .collect(Collectors.toList());

        return UserToothDTO.builder()
                .id(entity.getId())
                .toothNumber(entity.getTooth().getId())
                .toothType(entity.getTooth().getType().toString())
                .list(list)
                .userId(entity.getUser().getId())
                .build();
    }

    @Override
    public UserTooth toPersistence(UserToothDTO entity) {
        if (entity == null) {
            return null;
        }
        return userToothRepository.findById(entity.getId())
                .orElseGet(() -> {
                    UserTooth userTooth = new UserTooth();

                    Optional.ofNullable(entity.getId()).ifPresent(userTooth::setId);

                    toothRepository.findById(entity.getToothNumber()).ifPresent(userTooth::setTooth);

                    Optional.ofNullable(entity.getId())
                            .map(treatmentRepository::findAllByUserToothId)
                            .map(Lists::newArrayList)
                            .ifPresent(userTooth::setList);

                    Optional.ofNullable(entity.getId()).flatMap(userRepository::findById).ifPresent(userTooth::setUser);

                    return userTooth;
                });
    }
}
