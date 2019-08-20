create table profiles
(
    email        varchar(256) not null,
    first_name   varchar(256) not null,
    last_name    varchar(256) not null,
    phone_number varchar(256) not null,
    user_id      bigint       not null unique ,
    primary key (user_id)
);

create table ranks
(
    number_of_votes bigint,
    statistic       bigint,
    user_id         bigint not null unique ,
    primary key (user_id)
);

alter table ranks
    add constraint FK_ranks_users foreign key (user_id) references users (id);
alter table profiles
    add constraint FK_profiles_users foreign key (user_id) references users (id)