package com.scnsoft.authorities.persistence.repository;

import com.scnsoft.authorities.persistence.entity.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "authorities", path = "authorities")
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
    Optional<Authority> findAuthorityByName(@Param("name") String name);

    void deleteAuthorityByName(@Param("name") String name);
}
