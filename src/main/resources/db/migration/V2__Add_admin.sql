insert into permission_ (name)
values ('admin');

insert into permission_ (name)
values ('owner');

insert into user_permissions_ (permission_id, user_id, enabled)
values (2, 1, 0);

insert into group_ (name)
values ('admin');

insert into group_permissions_ (permission_id, group_id)
values (1, 1);

insert into user_(login, password, group_id)
VALUES ('Oleg', 'Savik', 1);
