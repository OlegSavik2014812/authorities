drop table if exists `groups`;
drop table if exists group_permissions;
drop table if exists permissions;
drop table if exists users;
drop table if exists user_permissions;

create table `groups`
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table group_permissions
(
    permission_id bigint not null,
    group_id      bigint not null
);

create table permissions
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table users
(
    id       bigint       not null auto_increment,
    login    varchar(255) not null,
    password varchar(255) not null,
    group_id bigint,
    primary key (id)
);

create table user_permissions
(
    permission_id bigint not null,
    user_id       bigint not null,
    enabled       TINYINT default 0,
    primary key (permission_id, user_id)
);

alter table group_permissions
    add constraint FK_group_permissions_group
        foreign key (group_id) references `groups` (id);

alter table group_permissions
    add constraint FK_group_permissions_permission
        foreign key (permission_id) references permissions (id);

alter table users
    add constraint FK_user_group
        foreign key (group_id) references `groups` (id);

alter table user_permissions
    add constraint FK_user_permissions_permission
        foreign key (permission_id) references permissions (id);

alter table user_permissions
    add constraint FK_user_permissions_user
        foreign key (user_id) references users (id);