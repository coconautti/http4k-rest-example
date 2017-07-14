create table users (
    id          varchar(36)     primary key,
    email       varchar(128)    unique not null,
    firstName   varchar(32)     not null,
    lastName    varchar(64)     not null,

    updated     timestamp       as current_timestamp
);

create index idx_users_email on users(email);
