package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.personal.RankDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.personal.Rank;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankConverter implements EntityConverter<Rank, RankDTO> {
    private final UserRepository userRepository;

    @Override
    public RankDTO toDTO(Rank entity) {
        return RankDTO.builder()
                .id(entity.getId())
                .numberOfVotes(entity.getNumberOfVotes())
                .statistic(entity.getStatistic())
                .build();
    }

    @Override
    public Rank toPersistence(RankDTO entityDTO) {
        Long id = entityDTO.getId();
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return Rank.builder()
                .id(id)
                .numberOfVotes(entityDTO.getNumberOfVotes())
                .statistic(entityDTO.getStatistic())
                .user(user)
                .build();
    }
}
