package edu.java.DTOModels.DTOjdbc;

import java.time.OffsetDateTime;

public record DTOLink(
        Long linkId,
        String url,
        OffsetDateTime updateAt,
        OffsetDateTime checkAt,
        String linkType,
        String data
) {
}
