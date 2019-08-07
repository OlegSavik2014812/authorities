package com.scnsoft.permissions.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
public class TreatmentDTO implements EntityDTO {
    private Long id;
    private LocalDate date;
    private BigDecimal cost;
    private String description;
    private Long userToothId;
}
