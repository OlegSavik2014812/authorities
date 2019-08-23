package com.scnsoft.permissions.dto.dental;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public  class BaseDentalRequestDTO implements EntityDTO<Long> {
    private Long id;
    private LocalDate date;
    private String description;
    private Long userToothId;
}