package com.scnsoft.permissions.dto.dental;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class ToothDTO implements EntityDTO<Long> {
    @Min(1)
    @Max(32)
    private Long id;
    @Pattern(regexp = "(?i)^(INCISOR|CANINE|PREMOLAR|MOLAR)$")
    private String type;
}