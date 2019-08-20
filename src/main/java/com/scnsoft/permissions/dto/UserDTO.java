package com.scnsoft.permissions.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO implements EntityDTO<Long> {
    private Long id;
    private String login;
    private String password;
    private String groupName;
    private List<String> permissions;
}