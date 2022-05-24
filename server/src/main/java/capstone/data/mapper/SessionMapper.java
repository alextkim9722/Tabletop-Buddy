package capstone.data.mapper;

import capstone.models.Session;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SessionMapper implements RowMapper<Session> {

    @Override
    public Session mapRow(ResultSet resultSet, int i) throws SQLException {

        long start = resultSet.getTimestamp("start_date").getTime();
        long end = resultSet.getTimestamp("end_date").getTime();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        sdf.applyPattern("yyyy-MM-dd hh:mm:ss");

        Session session = new Session(
                resultSet.getInt("session_id"),
                resultSet.getInt("campaign_id"),
                Timestamp.valueOf(sdf.format(start)),
                Timestamp.valueOf(sdf.format(end))
        );
        return session;
    }
}
