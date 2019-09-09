package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.personal.ProfileDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.personal.Profile;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileConverter implements EntityConverter<Profile, ProfileDTO> {
    private final UserRepository userRepository;

    @Override
    public ProfileDTO toDTO(Profile entity) {
        return ProfileDTO.builder()
                .id(entity.getUser().getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }

    @Override
    public Profile toPersistence(ProfileDTO entityDTO) {
        Long id = entityDTO.getId();
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return Profile.builder()
                .email(entityDTO.getEmail())
                .firstName(entityDTO.getFirstName())
                .lastName(entityDTO.getLastName())
                .phoneNumber(entityDTO.getPhoneNumber())
                .user(user)
                .id(id)
                .build();
    }
}