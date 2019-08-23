package com.scnsoft.permissions.service.personal;

import com.scnsoft.permissions.converter.ProfileConverter;
import com.scnsoft.permissions.dto.personal.ProfileDTO;
import com.scnsoft.permissions.persistence.entity.personal.Profile;
import com.scnsoft.permissions.persistence.repository.personal.ProfileRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class ProfileService extends BaseCrudService<Profile, ProfileDTO, Long> {
    public ProfileService(ProfileRepository repository, ProfileConverter converter) {
        super(repository, converter);
    }
}
