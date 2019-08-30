package com.scnsoft.permissions.dto.dental;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
public class UserToothDTO implements EntityDTO<Long> {
    private Long id;
    @NotNull
    private Long userId;
    @Pattern(regexp = "(?i)^(INCISOR|CANINE|PREMOLAR|MOLAR)$")
    private String toothType;
    private Long toothNumber;
    private List<TreatmentDTO> treatments;
    private List<ComplaintDTO> complaints;
}