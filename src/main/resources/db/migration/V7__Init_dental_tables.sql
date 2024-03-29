create table teeth
(
    id bigint not null auto_increment,
    type varchar(255) not null,
     primary key (id)
);

create table treatments
(
    id bigint not null auto_increment,
    cost decimal(19,2),
    date datetime,
    description varchar(255) not null,
    user_tooth_id bigint not null,
    primary key (id)
);

create table user_teeth
(
    id bigint not null auto_increment,
    tooth_id bigint not null,
    user_id bigint not null,
    primary key (id)
);

