package capstone.data;

import capstone.data.mapper.CampaignMapper;
import capstone.data.mapper.CampaignUserMapper;
import capstone.data.mapper.UserCampaignMapper;
import capstone.data.mapper.UserMapper;
import capstone.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

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

        user.setUserid(keyHolder.getKey().intValue());

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
                user.getUserid());

        updateRoles(user);
    }

    private void updateRoles(User user) {
        // delete all roles, then re-add
        jdbcTemplate.update("delete from user_role where user_id = ?;", user.getUserid());

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (String role : user.convertAuthoritiesToRoles(authorities)) {
            String sql = "insert into user_role (user_id, role_id) "
                    + "select ?, role_id from role where `name` = ?;";
            jdbcTemplate.update(sql, user.getUserid(), role);
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

    private void addHostedCampaigns(User user) {
        final String sql = "select campaign_id, user_id, `name`, `description`, `type`, city, "
                + "state, session_count, max_players "
                + "from campaign "
                + "where user_id = ?";


        var campaign = jdbcTemplate.query(sql, new CampaignMapper(), user.getUserid());
        user.setCampaignList(campaign);

    }

    private void addUserSchedule(User user) {
        final String sql = "select user_schedule_id, user_id, session_id, start_date, end_date"
                + "from user_schedule "
                + "where user_id = ?";

        var userSchedule = jdbcTemplate.query(sql, new User(), user.getUserid());
        user.setUserScheduleList(userSchedule);
    }

    private void addJoinedCampaign(User user) {
        final String sql = "select cu.campaign_id, cu.user_id, "
                + "cu.campaign_id, cu.user_id, cu.name, cu.description, cu.type, cu.city, cu.state, cu.session_count, cu.max_players"
                + "from campaign_user cu "
                + "inner join user u on cu.user_id = u.user_id"
                + "inner join campaign c on cu.campaign_id = c.campaign_id "
                + "where aa.agent_id = ?;";

        var campaignUsers = jdbcTemplate.query(sql, new UserCampaignMapper(), user.getUserid());
        user.setCampaignList(campaignUsers);
    }
}
