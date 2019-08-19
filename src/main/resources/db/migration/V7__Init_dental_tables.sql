create table teeth
(
    id   bigint       not null auto_increment,
    type varchar(255) not null,
    primary key (id)
);

create table treatments
(
    id            bigint       not null auto_increment,
    price         decimal(19, 2),
    date          datetime,
    description   varchar(255) not null,
    user_tooth_id bigint       not null,
    primary key (id)
);

create table user_teeth
(
    id       bigint not null,
    tooth_id bigint not null,
    user_id  bigint not null,
    primary key (id),
    unique (id)
);

create table complaints
(
    id            bigint       not null auto_increment,
    date          datetime,
    user_tooth_id bigint       not null,
    description   varchar(255) not null,
    primary key (id)
);