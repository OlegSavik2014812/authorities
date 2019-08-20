package com.scnsoft.permissions.persistence.repository.personal;

import com.scnsoft.permissions.persistence.entity.personal.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "profiles", path = "profiles")
public interface ProfileRepository extends CrudRepository<Profile, Long> {
}