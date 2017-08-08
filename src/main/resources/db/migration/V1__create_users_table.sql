create table users (
    id          bigint          auto_increment primary key,
    firstName   varchar(32)     not null,
    lastName    varchar(64)     not null,
    email       varchar(128)    unique not null,
    password    varchar(128)    not null
);

create index idx_users_email on users(email);
