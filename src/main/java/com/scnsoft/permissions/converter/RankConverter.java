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
        RankDTO rankDTO = new RankDTO();
        rankDTO.setId(entity.getId());
        rankDTO.setNumberOfVotes(entity.getNumberOfVotes());
        rankDTO.setStatistic(entity.getStatistic());
        return rankDTO;
    }

    @Override
    public Rank toPersistence(RankDTO entityDTO) {
        Rank rank = new Rank();
        rank.setId(entityDTO.getId());
        rank.setNumberOfVotes(entityDTO.getNumberOfVotes());
        rank.setStatistic(entityDTO.getStatistic());
        User user = userRepository.findById(entityDTO.getId()).orElseThrow(RuntimeException::new);
        rank.setUser(user);
        return rank;
    }
}
