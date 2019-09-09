package com.scnsoft.permissions.dto.personal;

import com.scnsoft.permissions.dto.EntityDTO;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankDTO implements EntityDTO<Long> {
    private Long id;
    private Long numberOfVotes;
    private Long statistic;
}
