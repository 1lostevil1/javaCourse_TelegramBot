create table chat
(
    chat_id    bigint                   not null,
    name       text                     not null,
    created_at timestamp with time zone not null,
    state      text                    not null ,

    primary key (chat_id)
)
