package com.scnsoft.permissions.dto.personal;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class ProfileDTO implements EntityDTO<Long> {
    private Long id;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phoneNumber;
}