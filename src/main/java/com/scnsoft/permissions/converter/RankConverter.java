package com.scnsoft.permissions.converter;

import com.scnsoft.permissions.dto.personal.RankDTO;
import com.scnsoft.permissions.persistence.entity.User;
import com.scnsoft.permissions.persistence.entity.personal.Rank;
import com.scnsoft.permissions.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class RankConverter implements EntityConverter<Rank, RankDTO> {
    private final UserRepository userRepository;

    public RankConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RankDTO toDTO(Rank entity) {
        RankDTO rankDTO = new RankDTO();
        rankDTO.setId(entity.getId());
        rankDTO.setNumberOfVotes(entity.getNumberOfVotes());
        rankDTO.setStatistic(entity.getStatistic());
        return rankDTO;
    }

    @Override
    public Rank toPersistence(RankDTO entity) {
        Rank rank = new Rank();
        rank.setId(entity.getId());
        rank.setNumberOfVotes(entity.getNumberOfVotes());
        rank.setStatistic(entity.getStatistic());
        User user = userRepository.findById(entity.getId()).orElseThrow(RuntimeException::new);
        rank.setUser(user);
        return rank;
    }
}
