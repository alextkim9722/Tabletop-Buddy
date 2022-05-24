package capstone.data.mapper;

import capstone.models.Session;
import capstone.models.UserSchedule;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserScheduleMapper implements RowMapper<UserSchedule> {

    @Override
    public UserSchedule mapRow(ResultSet resultSet, int i) throws SQLException {

        SessionMapper sessionMapper = new SessionMapper();

        long start = resultSet.getTimestamp("start_date").getTime();
        long end = resultSet.getTimestamp("end_date").getTime();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        sdf.applyPattern("yyyy-MM-dd hh:mm:ss");

        UserSchedule userSchedule = new UserSchedule(
                resultSet.getInt("user_schedule_id"),
                resultSet.getInt("user_id"),
                Timestamp.valueOf(sdf.format(start)),
                Timestamp.valueOf(sdf.format(end))
        );

        userSchedule.setSessionid(resultSet.getInt("session_id"));

        userSchedule.setSession(sessionMapper.mapRow(resultSet, i));

        return userSchedule;
    }
}
