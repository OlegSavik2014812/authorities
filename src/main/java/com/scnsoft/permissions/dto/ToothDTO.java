package com.scnsoft.permissions.dto;

import lombok.Data;

@Data
public class ToothDTO implements EntityDTO<Long> {
    private Long id;
    private String type;
}