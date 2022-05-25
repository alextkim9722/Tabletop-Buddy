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
    private final java.text.SimpleDateFormat sdf;

    public UserScheduleJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        sdf = new java.text.SimpleDateFormat();
        sdf.setTimeZone(java.util.TimeZone.getDefault());
        sdf.applyPattern("yyyy-MM-dd hh:mm:ss");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserSchedule create(UserSchedule session) {
        long start = session.getStartDate().getTime();
        long end = session.getEndDate().getTime();

        final String sql = "insert into user_schedule (user_schedule_id, user_id, session_id, start_date, end_date) " +
                " values (?,?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, session.getUserScheduleid());
            ps.setInt(2, session.getUserid());
            ps.setInt(3, session.getSessionid());
            ps.setTimestamp(4, Timestamp.valueOf(sdf.format(start)));
            ps.setTimestamp(5, Timestamp.valueOf(sdf.format(end)));
            // casted from java.util.Date to java.sql.Date
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        session.setUserScheduleid(keyHolder.getKey().intValue());
        return session;
    }

    @Override
    public boolean update(UserSchedule session) {
        long start = session.getStartDate().getTime();
        long end = session.getEndDate().getTime();

        final String sql = "update user_schedule set " +
                "start_date = ?, " +
                "end_date = ? " +
                "where user_schedule_id = ?;";

        return jdbcTemplate.update(sql,
                sdf.format(start),
                sdf.format(end),
                session.getUserScheduleid()) > 0;
    }

    @Override
    public boolean deleteById(int session_id) {
        return jdbcTemplate.update("delete from user_schedule where user_schedule_id = ?;", session_id) > 0;
    }

    @Override
    public List<UserSchedule> getFromUserId(int userid) {
        final String sql = "select s.user_schedule_id, s.user_id, s.session_id, s.start_date, s.end_date, se.campaign_id " +
                "from user_schedule s " +
                "inner join session se on s.session_id = se.session_id " +
                "where s.user_id = ?;";
        var sessions = jdbcTemplate.query(sql, new UserScheduleMapper(), userid);

        return sessions;
    }
}
