package com.scnsoft.permissions.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PermissionDTO implements EntityDTO {
    private Long id;
    private String name;
    private List<Long> groupIds;
    private Map<UserDTO, Boolean> additionalPermissions;
}