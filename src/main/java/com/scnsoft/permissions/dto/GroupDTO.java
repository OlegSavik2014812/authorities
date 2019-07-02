package com.scnsoft.permissions.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDTO implements EntityDTO{
    private Long id;
    private String name;
    private List<Long> userIds;
    private List<Long> permissionsIds;
}