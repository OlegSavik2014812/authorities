package com.scnsoft.authorities.dto;

import lombok.Data;

@Data
public class UserDTO implements EntityDTO {
    private Long id;
    private String login;
    private String password;
    private Long groupId;
}
