package edu.java.repository.mappers;

import edu.java.DTOModels.DTOjdbc.DTOLink;
import edu.java.DTOModels.DTOjdbc.DTOState;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;

public class StateMapper implements RowMapper<DTOState> {
    @Override
    public DTOState mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return new DTOState(
                rs.getString("state")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
