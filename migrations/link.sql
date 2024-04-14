--liquibase formatted sql

--changeset 1:1
create table if not exists link
(
    link text not null primary key,
    type_id integer not null,
    checked_date timestamp not null,
    update_date timestamp not null
);
