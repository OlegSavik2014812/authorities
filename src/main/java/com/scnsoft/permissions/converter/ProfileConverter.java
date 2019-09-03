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
        Profile profile = new Profile();
        profile.setEmail(entityDTO.getEmail());
        profile.setFirstName(entityDTO.getFirstName());
        profile.setLastName(entityDTO.getLastName());
        profile.setPhoneNumber(entityDTO.getPhoneNumber());
        User user = userRepository.findById(entityDTO.getId()).orElseThrow(RuntimeException::new);
        profile.setUser(user);
        profile.setId(user.getId());
        return profile;
    }
}