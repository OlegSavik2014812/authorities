insert into permissions (name)
values ('ADMIN');

insert into permissions (name)
values ('PATIENT');

insert into permissions (name)
values ('MODERATOR');

insert into permissions (name)
values ('OWNER');

insert into `groups` (name)
values ('ADMIN');

insert into `groups`(name)
values ('OWNER');

insert into users(login, password, group_id)
VALUES ('OlegSavik', '$2a$10$19.TUjECs76KbXEhJE4awutW9EOnv8lbq9Ya3q7pkYKn7ppukSueq', 2);

insert into users(login, password, group_id)
values ('Test123', '$2a$10$19.TUjECs76KbXEhJE4awutW9EOnv8lbq9Ya3q7pkYKn7ppukSueq', 1);