package edu.java.repository.mappers;

import edu.java.domain.DTOChatLink;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatLinkMapper implements RowMapper<DTOChatLink> {
    @Override
    public DTOChatLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new DTOChatLink(
                rs.getLong("chat_id"),
                rs.getLong("link_id")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
