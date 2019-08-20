package com.scnsoft.permissions.persistence.repository.personal;

import com.scnsoft.permissions.persistence.entity.personal.Rank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "ranks", path = "ranks")
public interface RankRepository extends CrudRepository<Rank, Long> {
}