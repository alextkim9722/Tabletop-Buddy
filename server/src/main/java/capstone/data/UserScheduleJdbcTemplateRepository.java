package capstone.data;

import capstone.data.mapper.SessionMapper;
import capstone.data.mapper.UserScheduleMapper;
import capstone.data.mapper.UserSessionMapper;
import capstone.models.Session;
import capstone.models.UserSchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class UserScheduleJdbcTemplateRepository implements UserScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    public UserScheduleJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserSchedule create(UserSchedule session) {
        final String sql = "insert into user_schedule (user_schedule_id, user_id, session_id, start_date, end_date) " +
                " values (?,?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, session.getUserScheduleId());
            ps.setInt(2, session.getUserId());
            ps.setObject(3, (session.getSessionId() == 0 ? null : session.getSessionId()));
            ps.setTimestamp(4, session.getStartDate());
            ps.setTimestamp(5, session.getEndDate());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        session.setUserScheduleId(keyHolder.getKey().intValue());
        return session;
    }

    @Override
    public boolean update(UserSchedule session) {

        final String sql = "update user_schedule set " +
                "start_date = ?, " +
                "end_date = ? " +
                "where user_schedule_id = ?;";

        return jdbcTemplate.update(sql,
                session.getStartDate(),
                session.getEndDate(),
                session.getUserScheduleId()) > 0;
    }

    @Override
    public boolean deleteById(int sessionId) {
        return jdbcTemplate.update("delete from user_schedule where user_schedule_id = ?;", sessionId) > 0;
    }

    @Override
    public UserSchedule getFromId(int id) {
        final String sql = "select s.user_schedule_id, s.user_id, s.session_id, s.start_date, s.end_date, se.campaign_id " +
                "from user_schedule s " +
                "left outer join session se on s.session_id = se.session_id " +
                "where s.user_schedule_id = ?;";
        var session = jdbcTemplate.query(sql, new UserScheduleMapper(), id).stream()
                .findFirst().orElse(null);

        return session;
    }

    @Override
    public List<UserSchedule> getFromUserId(int userId) {
        final String sql = "select s.user_schedule_id, s.user_id, s.session_id, s.start_date, s.end_date, se.campaign_id " +
                "from user_schedule s " +
                "left outer join session se on s.session_id = se.session_id " +
                "where s.user_id = ?;";
        var sessions = jdbcTemplate.query(sql, new UserScheduleMapper(), userId);

        return sessions;
    }

    @Override
    public boolean deleteBySessionId(int sessionId, int userId) {
        return jdbcTemplate.update("delete from user_schedule where session_id = ? and user_id = ?;", sessionId, userId) > 0;
    }
}
