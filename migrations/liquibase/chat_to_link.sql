create table chat_link
(
    chat_id bigint references chat(chat_id),
    link_id bigint references link(link_id),
    constraint id primary key (chat_id, link_id)

)
