package com.scnsoft.authorities.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthorityDTO implements EntityDTO{
    private Long id;
    private String name;
    private List<Long> groupIds;
}
