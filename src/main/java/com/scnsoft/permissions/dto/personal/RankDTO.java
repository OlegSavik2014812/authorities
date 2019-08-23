package com.scnsoft.permissions.dto.personal;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RankDTO implements EntityDTO<Long> {
    private Long id;
    private Long numberOfVotes;
    private Long statistic;
}
