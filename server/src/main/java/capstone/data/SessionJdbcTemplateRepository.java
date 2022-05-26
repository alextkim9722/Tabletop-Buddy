package capstone.data;

import capstone.data.mapper.SessionMapper;
import capstone.data.mapper.SessionUserMapper;
import capstone.models.Session;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class SessionJdbcTemplateRepository implements SessionRepository {

    private final JdbcTemplate jdbcTemplate;

    public SessionJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Session create(Session session) {

        final String sql = "insert into session (campaign_id, start_date, end_date) " +
                " values (?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, session.getCampaignId());
            ps.setTimestamp(2, session.getStartDate());
            ps.setTimestamp(3, session.getEndDate());
            // casted from java.util.Date to java.sql.Date
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        session.setSessionId(keyHolder.getKey().intValue());
        return session;
    }

    @Override
    public boolean update(Session session) {
        final String sql = "update session set " +
                "start_date = ?, " +
                "end_date = ? " +
                "where session_id = ?;";

        return jdbcTemplate.update(sql,
                session.getStartDate(),
                session.getEndDate(),
                session.getSessionId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int session_id) {
        jdbcTemplate.update("delete from user_schedule where session_id = ?;", session_id);
        jdbcTemplate.update("delete from session_user where session_id = ?;", session_id);
        return jdbcTemplate.update("delete from session where session_id = ?;", session_id) > 0;
    }

    @Override
    public List<Session> getFromUserId(int userId) {
        final String sql = "select s.session_id, s.campaign_id, s.start_date, s.end_date " +
                "from session s " +
                "inner join session_user su on s.session_id = su.session_id " +
                "where su.user_id = ?;";
        var sessions = jdbcTemplate.query(sql, new SessionMapper(), userId);

        for(Session s : sessions) {
            addUsers(s);
        }

        return sessions;
    }

    @Override
    public List<Session> getFromCampaignId(int CampaignId) {
        final String sql = "select s.session_id, s.campaign_id, s.start_date, s.end_date " +
                "from session s " +
                "inner join campaign c on s.campaign_id = c.campaign_id " +
                "where c.campaign_id = ?;";
        var sessions = jdbcTemplate.query(sql, new SessionMapper(), CampaignId);

        for(Session s : sessions) {
            addUsers(s);
        }

        return sessions;
    }

    private void addUsers(Session session) {

        final String sql = "select su.session_id, su.user_id, " +
                "u.user_id, u.username, u.city, u.state, u.`description`, u.password_hash, u.disabled, " +
                "s.session_id, s.campaign_id, s.start_date, s.end_date " +
                "from session_user su " +
                "inner join session s on su.session_id = s.session_id " +
                "inner join user u on su.user_id = u.user_id " +
                "where su.session_id = ?;";

        var sessionUsers = jdbcTemplate.query(sql, new SessionUserMapper(), session.getSessionId());
        session.setUserList(sessionUsers);
    }
}
