package com.scnsoft.permissions.persistence.repository.dentistry;

import com.scnsoft.permissions.persistence.entity.dentistry.Treatment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "treatments", path = "treatments")
public interface TreatmentRepository extends CrudRepository<Treatment, Long> {
}