package com.scnsoft.permissions.service.personal;

import com.scnsoft.permissions.converter.RankConverter;
import com.scnsoft.permissions.dto.personal.RankDTO;
import com.scnsoft.permissions.persistence.entity.personal.Rank;
import com.scnsoft.permissions.persistence.repository.personal.RankRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class RankService extends BaseCrudService<Rank, RankDTO, Long> {
    private RankRepository rankRepository;
    private RankConverter rankConverter;

    public RankService(RankRepository repository, RankConverter converter) {
        super(repository, converter);
        rankRepository = repository;
        rankConverter = converter;
    }

    public RankDTO vote(Long rankId, boolean isLike) {
        Rank rank = rankRepository.findById(rankId).orElseGet(Rank::new);
        Long numberOfVotes = rank.getNumberOfVotes();
        Long statistic = rank.getStatistic();
        if (isLike) {
            numberOfVotes = numberOfVotes + 1;
            statistic = statistic + 1;
        } else {
            numberOfVotes = numberOfVotes + 1;
            statistic = statistic - 1;
        }
        rank.setNumberOfVotes(numberOfVotes);
        rank.setStatistic(statistic);
        Rank save = rankRepository.save(rank);
        return rankConverter.toDTO(save);
    }
}