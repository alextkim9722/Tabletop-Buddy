package capstone.data;

import capstone.data.mapper.*;
import capstone.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class UserJdbcTemplateRepository implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {

        final String sql = "insert into user (username, password_hash, city, state, `description`) values (?, ?, ?, ?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getCity());
            ps.setString(4, user.getState());
            ps.setString(5, user.getDescription());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setUserId(keyHolder.getKey().intValue());

        updateRoles(user);

        return user;
    }

    @Override
    public User findByUsername(String username) {
        List<String> roles = getRolesByUsername(username);

        final String sql = "select user_id, username, password_hash, city, state, `description`, disabled "
                + "from user "
                + "where username = ?;";

        User user = jdbcTemplate.query(sql, new UserMapper(roles), username).stream()
                .findFirst().orElse(null);

        if (user != null) {
            addHostedCampaigns(user);
            addUserSchedule(user);
            addJoinedCampaign(user);
            addJoinedSessions(user);
        }

        return user;
    }

    @Override
    public User findById(int id) {
        List<String> roles = getRoledById(id);

        final String sql = "select user_id, username, password_hash, city, state, `description`, disabled "
                + "from user "
                + "where user_id = ?;";

        User user = jdbcTemplate.query(sql, new UserMapper(roles), id).stream()
                .findFirst().orElse(null);

        if (user != null) {
            addHostedCampaigns(user);
            addUserSchedule(user);
            addJoinedCampaign(user);
            addJoinedSessions(user);
        }

        return user;
    }

    @Override
    public void update(User user) {

        final String sql = "update user set "
                + "username = ?, "
                + "city = ?, "
                + "state = ?, "
                + "`description` = ?, "
                + "disabled = ? "
                + "where user_id = ?;";

        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getCity(),
                user.getState(),
                user.getDescription(),
                !user.isEnabled(),
                user.getUserId());

        updateRoles(user);
    }

    @Override
    @Transactional
    public boolean deleteById(int userId) {
        jdbcTemplate.update(
                "delete from session_user where user_id = ?;", userId
        );
        jdbcTemplate.update(
                "delete from user_schedule where user_id = ?;", userId
        );
        jdbcTemplate.update(
                "delete from user_role where user_id = ?;", userId
        );

        /* deleting sessions from schedules of users that come from campaign(s) that the user
        being deleted has made */
        jdbcTemplate.update(
                "delete us from user_schedule us "
                        + "inner join session s on us.session_id = s.session_id "
                        + "inner join campaign c on s.campaign_id = c.campaign_id "
                        + "inner join user u on c.user_id = u.user_id "
                        + "where u.user_id = ?;"
                , userId
        );
        jdbcTemplate.update(
                "delete su from session_user su " +
                        "inner join session s on su.session_id = s.session_id " +
                        "inner join campaign c on s.campaign_id = c.campaign_id " +
                        "where c.user_id = ?;"
                , userId
        );
        /* deleting sessions that are a part of the campaign(s)
        that the user being deleted has made */
        jdbcTemplate.update(
                "delete s from session s "
                        + "inner join campaign c on s.campaign_id = c.campaign_id "
                        + "where c.user_id = ?;"
                , userId
        );
        jdbcTemplate.update(
                "delete cu from campaign_user cu " +
                        "inner join campaign c on cu.campaign_id = c.campaign_id " +
                        "where c.user_id = ?;"
                , userId
        );
        jdbcTemplate.update(
                "delete from campaign_user " +
                        "where user_id = ?;"
                , userId
        );
        // finally, deleting campaigns that the user has created
        jdbcTemplate.update(
                "delete from campaign " +
                        "where user_id = ?;"
                , userId
        );
        return jdbcTemplate.update(
                "delete from user " +
                        "where user_id = ?;"
                , userId
        ) > 0;

    }

    private void updateRoles(User user) {
        // delete all roles, then re-add
        jdbcTemplate.update("delete from user_role where user_id = ?;", user.getUserId());

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (String role : user.convertAuthoritiesToRoles(authorities)) {
            String sql = "insert into user_role (user_id, role_id) "
                    + "select ?, role_id from role where `name` = ?;";
            jdbcTemplate.update(sql, user.getUserId(), role);
        }
    }

    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.name "
                + "from user_role ur "
                + "inner join role r on ur.role_id = r.role_id "
                + "inner join user au on ur.user_id = au.user_id "
                + "where au.username = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), username);
    }

    private List<String> getRoledById(int id) {
        final String sql = "select r.name "
                + "from user_role ur "
                + "inner join role r on ur.role_id = r.role_id "
                + "where ur.user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), id);
    }

    private void addHostedCampaigns(User user) {
        final String sql = "select campaign_id, user_id, `name`, `description`, `type`, city, "
                + "state, session_count, max_players, current_players "
                + "from campaign "
                + "where user_id = ?";
        var campaign = jdbcTemplate.query(sql, new CampaignMapper(), user.getUserId());
        user.setHostedCampaignList(campaign);
    }

    private void addUserSchedule(User user) {
        final String sql = "select us.user_schedule_id, us.user_id, us.session_id, us.start_date, us.end_date, s.campaign_id "
                + "from user_schedule us "
                + "inner join session s on s.session_id = us.session_id "
                + "where us.user_id = ?";

        var userSchedule = jdbcTemplate.query(sql, new UserScheduleMapper(), user.getUserId());
        user.setUserScheduleList(userSchedule);
    }

    private void addJoinedCampaign(User user) {
        final String sql = "select cu.campaign_id, cu.user_id, c.`name`, c.`description`, "
                + "c.`type`, c.city, c.state, c.session_count, c.max_players, c.current_players "
                + "from campaign_user cu "
                + "inner join user u on cu.user_id = u.user_id "
                + "inner join campaign c on cu.campaign_id = c.campaign_id "
                + "where cu.user_id = ?;";

        var userCampaigns = jdbcTemplate.query(sql, new UserCampaignMapper(), user.getUserId());
        user.setCampaignList(userCampaigns);
    }

    private void addJoinedSessions(User user) {
        final String sql = "select su.user_id, su.session_id, s.campaign_id, s.start_date, s.end_date "
                + "from session_user su "
                + "inner join user u on su.user_id = u.user_id "
                + "inner join session s on su.session_id = s.session_id "
                + "where su.user_id = ?;";

        var userSessions = jdbcTemplate.query(sql, new UserSessionMapper(), user.getUserId());
        user.setSessionList(userSessions);
    }


}