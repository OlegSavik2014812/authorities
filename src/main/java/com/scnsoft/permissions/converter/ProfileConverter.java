package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.personal.ProfileDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.personal.Profile;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverter implements EntityConverter<Profile, ProfileDTO> {
    private final UserRepository userRepository;

    public ProfileConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
    public Profile toPersistence(ProfileDTO entity) {
        Profile profile = new Profile();
        profile.setEmail(entity.getEmail());
        profile.setFirstName(entity.getFirstName());
        profile.setLastName(entity.getLastName());
        profile.setPhoneNumber(entity.getPhoneNumber());
        User user = userRepository.findById(entity.getId()).orElseThrow(RuntimeException::new);
        profile.setUser(user);
        profile.setId(user.getId());
        return profile;
    }
}