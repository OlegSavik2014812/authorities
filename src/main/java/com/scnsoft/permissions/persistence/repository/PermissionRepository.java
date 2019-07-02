package com.scnsoft.permissions.persistence.repository;

import com.scnsoft.permissions.persistence.entity.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "permissions", path = "permissions")
public interface PermissionRepository extends CrudRepository<Permission, Long> {
    Optional<Permission> findPermissionByName(@Param("name") String name);

    void deletePermissionByName(@Param("name") String name);
}