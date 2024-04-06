package edu.java.repository.mappers;

import edu.java.DTOModels.DTOjdbc.DTOLink;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;
@SuppressWarnings({"MultipleStringLiterals", "MagicNumber"})

public class LinkMapper implements RowMapper<DTOLink> {
    @Override
    public DTOLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new DTOLink(
                rs.getLong("link_id"),
                rs.getString("url"),
                rs.getTimestamp("update_at").toLocalDateTime().atOffset(ZoneOffset.UTC).minusHours(3),
                rs.getTimestamp("check_at").toLocalDateTime().atOffset(ZoneOffset.UTC),
                rs.getString("link_type"),
                rs.getString("data")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
