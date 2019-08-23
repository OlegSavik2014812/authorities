package com.scnsoft.permissions.dto.dental;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.Data;

@Data
public class ToothDTO implements EntityDTO<Long> {
    private Long id;
    private String type;
}