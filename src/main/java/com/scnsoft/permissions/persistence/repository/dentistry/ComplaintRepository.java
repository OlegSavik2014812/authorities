package com.scnsoft.permissions.persistence.repository.dentistry;

import com.scnsoft.permissions.persistence.entity.dentistry.Complaint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "complaints", path = "complaints")
public interface ComplaintRepository extends CrudRepository<Complaint, Long> {

    Iterable<Complaint> findAllByUserToothId(@Param("id") Long id);
}
