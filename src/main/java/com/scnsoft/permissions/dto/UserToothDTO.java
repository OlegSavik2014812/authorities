package com.scnsoft.permissions.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserToothDTO implements EntityDTO {
    private Long id;
    private Long userId;
    private String toothType;
    private Long toothNumber;
    private List<TreatmentDTO> list;
}
