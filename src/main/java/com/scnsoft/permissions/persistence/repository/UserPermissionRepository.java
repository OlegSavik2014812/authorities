package com.scnsoft.permissions.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserPermissionRepository {
    Page<Long> findUserIdsByPermissions(List<String> permissions, Pageable pageable);
}
