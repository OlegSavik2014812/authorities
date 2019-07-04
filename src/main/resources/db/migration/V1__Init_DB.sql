drop table if exists group_;
drop table if exists group_permissions_;
drop table if exists permission_;
drop table if exists user_;
drop table if exists user_permissions_;

create table group_
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = MyISAM;

create table group_permissions_
(
    permission_id bigint not null,
    group_id      bigint not null
) engine = MyISAM;

create table permission_
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = MyISAM;

create table user_
(
    id       bigint       not null auto_increment,
    login    varchar(255) not null,
    password varchar(255) not null,
    group_id bigint,
    primary key (id)
) engine = MyISAM;

create table user_permissions_
(
    permission_id bigint not null,
    user_id       bigint not null,
    enabled       TINYINT default 0,
    primary key (permission_id, user_id)
) engine = MyISAM;

alter table group_permissions_
    add constraint FK_group_permissions_group
        foreign key (group_id) references group_ (id);

alter table group_permissions_
    add constraint FK_group_permissions_permission
        foreign key (permission_id) references permission_ (id);

alter table user_
    add constraint FK_user_group
        foreign key (group_id) references group_ (id);

alter table user_permissions_
    add constraint FK_user_permissions_permission
        foreign key (permission_id) references permission_ (id);

alter table user_permissions_
    add constraint FK_user_permissions_user
        foreign key (user_id) references user_ (id);