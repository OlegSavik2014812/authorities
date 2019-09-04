package com.scnsoft.permissions.persistence.repository;

import com.scnsoft.permissions.persistence.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findUserByLogin(@Param("login") String login);

    boolean existsByLogin(@Param("login") String login);

    @Query("select user from User user where login in :logins")
    Iterable<User> findUsersByNames(@Param("logins") Iterable<String> loginList);

    @Query("select user from User user join user.additionalPermissions perms on ")
    Iterable<User> findUsersByAdditionalPermissionsWithPermission(@Param("permissions") Iterable<String> permissions);
}
/*get users by special permission

* SELECT users.id, users.login, users.password, users.group_id
FROM user_perms.users as users
inner join
(	select grps.id as group_id from user_perms.groups as grps
inner join user_perms.group_permissions grp_perms on grp_perms.group_id = grps.id
inner join user_perms.permissions perms on perms.id = grp_perms.permission_id
where perms.name = 'OWNER'
    ) patient_grp on users.group_id = patient_grp.group_id

    union

select users.id, users.login, users.password, users.group_id from user_perms.users as users
inner join user_perms.user_permissions usr_perms on usr_perms.user_id = users.id & usr_perms.enabled = true
inner join user_perms.permissions perms on usr_perms.permission_id = perms.id
where perms.name  ='OWNER'
* */