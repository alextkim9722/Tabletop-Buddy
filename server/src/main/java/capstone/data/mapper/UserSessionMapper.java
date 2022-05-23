package capstone.data.mapper;

import capstone.models.UserSession;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserSessionMapper implements RowMapper<UserSession> {

    @Override
    public UserSession mapRow(ResultSet resultSet, int i) throws SQLException {
        /*
        SessionMapper sessionMapper = new SessionMapper();

        UserSession userSession = new UserSession(
                resultSet.getInt("user_id"),
                sessionMapper.mapRow(resultSet, i)
        );
        return userSession;
         */

        return null;
    }
}
