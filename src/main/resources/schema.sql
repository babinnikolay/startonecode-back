drop table if exists lessons cascade;
drop table if exists s_users cascade;
drop sequence if exists lessons_seq;
create sequence lessons_seq start with 1 increment by 50;
create table lessons (
                         id integer not null,
                         is_available boolean not null,
                         is_done boolean not null,
                         description varchar(255),
                         name varchar(255),
                         primary key (id)
);
create table s_users (
                         uuid uuid not null,
                         email varchar(255) not null unique,
                         name varchar(255) not null unique,
                         password varchar(255) not null,
                         primary key (uuid)
);