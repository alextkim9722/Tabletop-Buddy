package capstone.data;

import capstone.models.Session;
import capstone.models.SessionUser;
import capstone.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;

@Repository
public class SessionUserJdbcTemplateRepository implements SessionUserRepository{

    private final JdbcTemplate jdbcTemplate;

    public SessionUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(SessionUser su) {
        final String sql = "insert into session_user (session_id, user_id) values "
                + "(?,?);";

        return jdbcTemplate.update(sql,
                su.getSessionid(),
                su.getUser().getUserid()) > 0;
    }

    @Override
    public boolean delete(int sessionId, int userId) {
        final String sql = "delete from session_user "
                + "where session_id = ? and user_id = ?;";

        return jdbcTemplate.update(sql, sessionId, userId) > 0;
    }
}
