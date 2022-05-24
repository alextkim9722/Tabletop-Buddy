package capstone.data.mapper;

import capstone.models.Session;
import capstone.models.UserSchedule;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserScheduleMapper implements RowMapper<UserSchedule> {

    @Override
    public UserSchedule mapRow(ResultSet resultSet, int i) throws SQLException {

        SessionMapper sessionMapper = new SessionMapper();

        UserSchedule userSchedule = new UserSchedule(
                resultSet.getInt("user_schedule_id"),
                resultSet.getDate("start_date"),
                resultSet.getDate("end_date")
        );

        userSchedule.setSession(sessionMapper.mapRow(resultSet, i));

        return userSchedule;
    }
}
