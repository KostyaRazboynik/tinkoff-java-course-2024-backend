--liquibase formatted sql

--changeset 1:1
create table if not exists link_chat
(
    link text,
    chat_id bigint,
    primary key (link, chat_id),
    foreign key (link) references link (link),
    foreign key (chat_id) references chat (chat_id)
)
