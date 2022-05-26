package capstone.data.mapper;

import capstone.models.Session;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SessionMapper implements RowMapper<Session> {

    @Override
    public Session mapRow(ResultSet resultSet, int i) throws SQLException {
        Session session = new Session(
                resultSet.getInt("session_id"),
                resultSet.getInt("campaign_id"),
                resultSet.getTimestamp("start_date"),
                resultSet.getTimestamp("end_date")
        );
        return session;
    }
}
