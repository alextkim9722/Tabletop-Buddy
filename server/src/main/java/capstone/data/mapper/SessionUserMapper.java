package capstone.data.mapper;

import capstone.models.SessionUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SessionUserMapper implements RowMapper<SessionUser> {

    @Override
    public SessionUser mapRow(ResultSet resultSet, int i) throws SQLException {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("NA");
        UserMapper usermapper = new UserMapper(roles);

        SessionUser userSession = new SessionUser(
                resultSet.getInt("session_id"),
                usermapper.mapRow(resultSet, i)
        );
        return userSession;
    }
}
