package edu.java.domain;

import java.time.OffsetDateTime;

public record DTOChat(
    Long chatId,
    String name,
    OffsetDateTime createdAt
) {
}
