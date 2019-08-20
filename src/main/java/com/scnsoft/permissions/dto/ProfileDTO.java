package com.scnsoft.permissions.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ProfileDTO implements EntityDTO<Long> {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}