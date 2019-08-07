package com.scnsoft.permissions.persistence.repository.dentistry;

import com.scnsoft.permissions.persistence.entity.dentistry.UserTooth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "user_tooth", path = "user_tooth")
public interface UserToothRepository extends CrudRepository<UserTooth, Long> {
    Iterable<UserTooth> findAllByUserId(@Param("id") Long id);
}
