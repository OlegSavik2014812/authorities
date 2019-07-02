package com.scnsoft.permissions.persistence.repository;

import com.scnsoft.permissions.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "users", path = "users  ")
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByLogin(@Param("login") String login);

    boolean existsByLogin(@Param("login") String login);
}