package edu.java.DTOModels.DTOjdbc;

import java.time.OffsetDateTime;

public record DTOChat(
    Long chatId,
    String name,
    OffsetDateTime createdAt
) {
}
