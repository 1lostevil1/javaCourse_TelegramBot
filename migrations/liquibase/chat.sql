create table chat
(
    chat_id    bigint                   not null,
    name       text,

    created_at timestamp with time zone not null,

    primary key (chat_id),
)
