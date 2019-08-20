package com.scnsoft.permissions.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RankDTO implements EntityDTO<Long> {
    private Long id;
    private Long numberOfVotes;
    private Long statistic;
}
