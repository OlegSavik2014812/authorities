package com.scnsoft.permissions.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserPermissionRepositoryImpl implements UserPermissionRepository {
    private static final String QUERY =
            "SELECT users.id\n" +
                    "FROM user_perms.users as users \n" +
                    "inner join (\n" +
                    "\tselect grps.id  \n" +
                    "    from user_perms.groups as grps\n" +
                    "    inner join user_perms.group_permissions grp_perms on grp_perms.group_id = grps.id\n" +
                    "    inner join user_perms.permissions perms on perms.id = grp_perms.permission_id\n" +
                    "    where perms.name in (?1)\n )" +
                    "patient_grp on users.group_id = patient_grp.id\n" +
                    "union \n" +
                    "select users.id\n" +
                    "from user_perms.users as users\n" +
                    "inner join user_perms.user_permissions usr_perms on usr_perms.user_id = users.id & usr_perms.enabled = 1\n" +
                    "inner join user_perms.permissions perms on usr_perms.permission_id = perms.id where perms.name  in (?1) \n" +
                    "limit ?2, ?3";
    private static final String COUNT_QUERY =
            " SELECT COUNT(*) FROM (SELECT users.id\n" +
                    "FROM user_perms.users as users \n" +
                    "inner join (\n" +
                    "\tselect grps.id  \n" +
                    "    from user_perms.groups as grps\n" +
                    "    inner join user_perms.group_permissions grp_perms on grp_perms.group_id = grps.id\n" +
                    "    inner join user_perms.permissions perms on perms.id = grp_perms.permission_id\n" +
                    "    where perms.name in (?1)\n" +
                    ") " +
                    " patient_grp on users.group_id = patient_grp.id\n" +
                    "\n" +
                    "union \n" +
                    "\n" +
                    "select users.id\n" +
                    "from user_perms.users as users\n" +
                    "inner join user_perms.user_permissions usr_perms on usr_perms.user_id = users.id & usr_perms.enabled = 1\n" +
                    "inner join user_perms.permissions perms on usr_perms.permission_id = perms.id where perms.name  in (?1)) x";
    private final EntityManager manager;

    @Override
    public Page<Long> findUserIdsByPermissions(List<String> permissions, Pageable pageable) {
        Query nativeQuery = manager.createNativeQuery(QUERY)
                .setParameter(1, permissions)
                .setParameter(2, pageable.getPageNumber())
                .setParameter(3, pageable.getPageSize());

        List<BigInteger> list = nativeQuery.getResultList();
        List<Long> userIds = list.stream().map(BigInteger::longValue).collect(Collectors.toList());

        Object result = manager.createNativeQuery(COUNT_QUERY).setParameter(1, permissions).getSingleResult();
        long totalCount = Long.parseLong(result.toString());

        return new PageImpl<>(userIds, pageable, totalCount);
    }
}
