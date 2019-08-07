package com.scnsoft.permissions.persistence.repository.permission;

import com.scnsoft.permissions.persistence.entity.permission.AdditionalPermission;
import com.scnsoft.permissions.persistence.entity.permission.CompositePermissionId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AdditionalPermissionRepository extends CrudRepository<AdditionalPermission, CompositePermissionId> {

}