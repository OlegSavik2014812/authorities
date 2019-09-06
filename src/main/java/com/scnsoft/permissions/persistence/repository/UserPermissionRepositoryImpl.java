package com.scnsoft.permissions.persistence.repository;

import com.scnsoft.permissions.util.ExecutionTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class UserPermissionRepositoryImpl implements UserPermissionRepository {
    private static final String LIMIT_QUERY_PATTERN = "\t %s limit ?2,?3";
    private static final String COUNT_QUERY_PATTERN = "select count(*) \tfrom( %s ) x";
    private static final String USERS_WITH_PERMISSION =
            "select id \n" +
                    "\t from( \n" +
                    "\t\t select users.id as id\n" +
                    "\t\t FROM user_perms.users as users \n" +
                    "\t\t inner join user_perms.groups grps on users.group_id = grps.id\n" +
                    "\t\t inner join user_perms.group_permissions grp_perms on grp_perms.group_id = grps.id\n" +
                    "\t\t inner join user_perms.permissions perms on perms.id = grp_perms.permission_id \n" +
                    "\t\t where perms.name in (?1)\n" +
                    "\t\t ) x\n" +
                    "\t left join (\n" +
                    "\t\t select users.id as additional_perm_user_id, usr_perms.enabled\n" +
                    "\t\t from user_perms.users as users\n" +
                    "\t\t inner join user_perms.user_permissions usr_perms on usr_perms.user_id = users.id \n" +
                    "\t\t inner join user_perms.permissions perms on usr_perms.permission_id = perms.id \n" +
                    "\t\t where perms.name in (?1)\n" +
                    "    ) y \n" +
                    "on x.id  = y.additional_perm_user_id\n" +
                    "where enabled is null or enabled = true ";

    private static final String ADDITIONAL_PERMISSIONS_USERS =
            "select users.id as additional_perm_user_id\n" +
                    "from user_perms.users as users\n" +
                    "inner join user_perms.user_permissions usr_perms on usr_perms.user_id = users.id\n" +
                    "inner join user_perms.permissions perms on usr_perms.permission_id = perms.id and perms.name in(?1)";
    private final BiFunction<String, Map<Integer, Object>, Query> queryMapper;

    public UserPermissionRepositoryImpl(EntityManager manager) {
        queryMapper = (queryString, args) -> {
            Query query = manager.createNativeQuery(queryString);
            for (Map.Entry<Integer, Object> argument : args.entrySet()) {
                query.setParameter(argument.getKey(), argument.getValue());
            }
            return query;
        };
    }

    @ExecutionTime
    @Override
    public Page<Long> findUserIdsByPermissions(List<String> permissions, Pageable pageable) {
        return getLongs(permissions, pageable, USERS_WITH_PERMISSION);
    }

    public Page<Long> findUserIdsByAssignedPermissions(List<String> permissions, Pageable pageable) {
        return getLongs(permissions, pageable, ADDITIONAL_PERMISSIONS_USERS);
    }

    private Page<Long> getLongs(List<String> permissions, Pageable pageable, String queryString) {
        Map<Integer, Object> limitedArgs = Map.of(1, permissions, 2, pageable.getPageNumber(), 3, pageable.getPageSize());
        Map<Integer, Object> countArgs = Map.of(1, permissions);
        List<Long> userIds = getLimitedUserIds(queryString, limitedArgs);
        long total = countRaws(queryString, countArgs);
        return new PageImpl<>(userIds, pageable, total);
    }

    private List<Long> getLimitedUserIds(String queryString, Map<Integer, Object> args) {
        String limitedQueryString = String.format(LIMIT_QUERY_PATTERN, queryString);
        Query query = queryMapper.apply(limitedQueryString, args);
        List<Object> list = query.getResultList();
        return list.stream()
                .map(o -> ((BigInteger) o).longValue())
                .collect(Collectors.toList());
    }

    private long countRaws(String queryString, Map<Integer, Object> args) {
        String countedQueryString = String.format(COUNT_QUERY_PATTERN, queryString);
        Query query = queryMapper.apply(countedQueryString, args);
        String count = query.getSingleResult().toString();
        return Long.parseLong(count);
    }
}