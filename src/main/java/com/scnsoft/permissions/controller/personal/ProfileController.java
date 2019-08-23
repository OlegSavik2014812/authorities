package com.scnsoft.permissions.controller.personal;

import com.scnsoft.permissions.dto.personal.ProfileDTO;
import com.scnsoft.permissions.service.personal.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "profiles")
@RequestMapping("profiles")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping(value = "{id}")
    public ProfileDTO getProfile(@PathVariable(value = "id") Long userId) {
        return profileService.findById(userId).orElseThrow(NullPointerException::new);
    }
}