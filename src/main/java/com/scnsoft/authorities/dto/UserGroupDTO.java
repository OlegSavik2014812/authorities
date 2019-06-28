package com.scnsoft.authorities.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserGroupDTO implements EntityDTO{
    private Long id;
    private String name;
    private List<Long> userIds;
    private List<Long> authoritiesIds;
}
