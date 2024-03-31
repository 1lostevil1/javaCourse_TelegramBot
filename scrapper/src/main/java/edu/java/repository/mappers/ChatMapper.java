package edu.java.repository.mappers;

import edu.java.DTOModels.DTOjdbc.DTOChat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;

public class ChatMapper implements RowMapper<DTOChat> {

    @Override
    public DTOChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new DTOChat(
                rs.getLong("chat_id"),
                rs.getString("name"),
                rs.getTimestamp("created_at").toLocalDateTime().atOffset(ZoneOffset.UTC),
                rs.getString("state")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
