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

        UserSchedule userSchedule = new UserSchedule(
                resultSet.getInt("user_schedule_id"),
                resultSet.getInt("user_id"),
                resultSet.getTimestamp("start_date"),
                resultSet.getTimestamp("end_date")
        );

        userSchedule.setSessionId(resultSet.getInt("session_id"));

        userSchedule.setSession(sessionMapper.mapRow(resultSet, i));

        return userSchedule;
    }
}
