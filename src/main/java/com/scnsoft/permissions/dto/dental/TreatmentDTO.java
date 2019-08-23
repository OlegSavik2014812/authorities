package com.scnsoft.permissions.dto.dental;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class TreatmentDTO extends BaseDentalRequestDTO {
    private BigDecimal price;
}