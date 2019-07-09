package com.scnsoft.permissions.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UserDTO implements EntityDTO {
    private Long id;
    private String login;
    private String password;
    private String groupName;
    private Map<String, Boolean> additionalPermissions;
}