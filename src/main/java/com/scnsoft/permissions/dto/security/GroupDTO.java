package com.scnsoft.permissions.dto.security;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class GroupDTO implements EntityDTO<Long> {
    private Long id;
    @NotBlank
    private String name;
    private List<String> userNames;
    private List<String> permissionNames;
}