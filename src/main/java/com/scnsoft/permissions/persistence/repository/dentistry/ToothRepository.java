package com.scnsoft.permissions.persistence.repository.dentistry;

import com.scnsoft.permissions.persistence.entity.dentistry.Tooth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "teeth", path = "teeth")
public interface ToothRepository extends CrudRepository<Tooth, Long> {
}