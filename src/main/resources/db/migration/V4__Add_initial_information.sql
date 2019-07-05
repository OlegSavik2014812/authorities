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
