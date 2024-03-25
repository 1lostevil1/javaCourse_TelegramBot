package edu.java.repository.mappers;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import edu.java.DTOModels.DTOjdbc.DTOLink;
import org.springframework.jdbc.core.RowMapper;
public class LinkMapper implements RowMapper<DTOLink>{
    @Override
    public DTOLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new DTOLink(
                rs.getLong("link_id"),
                rs.getString("url"),
                rs.getTimestamp("update_at").toLocalDateTime().atOffset(ZoneOffset.UTC),
                rs.getTimestamp("check_at").toLocalDateTime().atOffset(ZoneOffset.UTC),
                rs.getString("link_type"),
                rs.getString("data")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}