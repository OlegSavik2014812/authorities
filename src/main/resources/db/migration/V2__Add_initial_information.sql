insert into permissions (name)
values ('admin');

insert into permissions (name)
values ('moderator');

insert into permissions (name)
values ('owner');

insert into `groups` (name)
values ('admin');

insert into users(login, password, group_id)
VALUES ('Oleg', 'Savik', 1);

insert into user_permissions (permission_id, user_id, enabled)
values (3, 1, 0);
insert into user_permissions (permission_id, user_id, enabled)
values (2, 1, 1);

insert into group_permissions (permission_id, group_id)
values (1, 1);
insert into group_permissions (permission_id, group_id)
values (2, 1);
