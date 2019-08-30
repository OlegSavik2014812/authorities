package com.scnsoft.permissions.dto.dental;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BaseDentalRequestDTO implements EntityDTO<Long> {
    private Long id;
    private LocalDateTime date;
    @NotBlank
    private String description;
    @NotNull
    private Long userToothId;
}