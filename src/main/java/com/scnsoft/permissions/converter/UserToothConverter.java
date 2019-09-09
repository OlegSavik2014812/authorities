package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.dental.UserToothDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import com.scnsoft.permissions.persistence.repository.dentistry.ToothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        return UserToothDTO.builder()
                .id(entity.getId())
                .toothNumber(tooth.getId())
                .toothType(tooth.getType().toString())
                .userId(entity.getUser().getId())
                .complaints(ConverterUtils.transform(entity.getComplaints(), complaintConverter::toDTO))
                .treatments(ConverterUtils.transform(entity.getTreatments(), treatmentConverter::toDTO))
                .build();
    }

    @Override
    public UserTooth toPersistence(UserToothDTO entityDTO) {
        UserTooth userTooth = new UserTooth();
        Long id = entityDTO.getId();
        userTooth.setId(id);
        Tooth tooth = toothRepository.findById(entityDTO.getToothNumber())
                .orElseThrow(() -> new UnsupportedOperationException("Tooth should be specified"));
        User user = userRepository.findById(entityDTO.getUserId())
                .orElseThrow(() -> new UnsupportedOperationException("User should be specified"));
        userTooth.setTooth(tooth);
        userTooth.setUser(user);
        return userTooth;
    }
}