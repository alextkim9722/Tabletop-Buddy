package capstone.data.mapper;

import capstone.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserMapper implements RowMapper<User> {

    private final List<String> roles;

    public UserMapper(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {

        User user = new User(
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("password_hash"),
                resultSet.getBoolean("disabled"),
                roles);

        user.setCity(resultSet.getString("city"));
        user.setState(resultSet.getString("state"));

        return user;
    }
}