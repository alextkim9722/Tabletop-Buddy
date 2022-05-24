package capstone.data;

import capstone.data.mapper.CampaignMapper;
import capstone.data.mapper.CampaignUserMapper;
import capstone.data.mapper.SessionMapper;
import capstone.data.mapper.SessionUserMapper;
import capstone.models.Campaign;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CampaignJdbcTemplateRepository implements CampaignRepository{
    private final JdbcTemplate jdbcTemplate;

    public CampaignJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}


    @Override
    public List<Campaign> findAll() {
        final String sql = "select campaign_id, user_id, name, description, type, " +
                "city, state, session_count, max_players, current_players from campaign limit 1000;";
        return jdbcTemplate.query(sql,new CampaignMapper());
    }

    @Override
    public Campaign findById(int campaignId) {
        final String sql = "select campaign_id, user_id, name, description, type, " +
                "city, state, session_count, max_players, current_players "
                + "from campaign "
                + "where campaign_id = ?;";

        Campaign result = jdbcTemplate.query(sql, new CampaignMapper(), campaignId).stream()
                .findAny().orElse(null);

        if (result != null) {
            addSessions(result);
            addUsers(result);
        }

        return result;
    }

    @Override
    public List<Campaign> findbyTag(String type, int players, int size, Date start) {
        final String sql = "select c.campaign_id, c.user_id, c.name, c.description, c.type, "
                + "c.city, c.state, c.session_count, c.max_players, c.current_players "
                + "from session s "
                + "inner join campaign c on c.campaign_id = s.campaign_id "
                + "where "
                + "c.type = " + (type == null ? "c.type" : "\""+type+"\"") + " "
                + "and c.current_players = " + (players == -1 ? "c.current_players" : players) + " "
                + "and c.session_count = " + (size == -1 ? "c.session_count" : size) + " "
                + "and s.start_date = " + (start == null ? "s.start_date" : "\""+start.toString()+"\"") + " "
                + "group by c.campaign_id, c.user_id, c.name, c.description, c.type, "
                + "c.city, c.state, c.session_count, c.max_players, c.current_players;";

        var result = jdbcTemplate.query(sql, new CampaignMapper());
        return result;
    }


    @Override
    public Campaign add(Campaign campaign) {
        final String sql = "insert into campaign (name, description, type, " +
                "city, state, session_count, max_players, user_id) values (?,?,?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, campaign.getName());
            ps.setString(2, campaign.getDescription());
            ps.setString(3,campaign.getType());
            ps.setString(4, campaign.getCity());
            ps.setString(5, campaign.getState());
            ps.setInt(6, campaign.getSessionCount());
            ps.setInt(7, campaign.getMaxPlayers());
            ps.setInt(8, campaign.getUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        campaign.setCampaignId(keyHolder.getKey().intValue());
        return campaign;
    }

    @Override
    public boolean update(Campaign campaign) {
        final String sql = "update campaign set"
                + "name = ?, "
                + "description = ?, "
                + "type = ?, "
                + "city = ?, "
                + "state = ?, "
                + "session_count = ?, "
                + "max_players = ?,"
                + "current_players = ?, "
                + "where campaign_id = ?;";

        return jdbcTemplate.update(sql,
                campaign.getName(),
                campaign.getDescription(),
                campaign.getType(),
                campaign.getCity(),
                campaign.getState(),
                campaign.getSessionCount(),
                campaign.getCurrentPlayers(),
                campaign.getMaxPlayers()) >0;
    }

    @Override
    @Transactional
    public boolean deleteById(int campaignId) {
        jdbcTemplate.update(
                "delete us from user_schedule us "
                        + "inner join session s on us.session_id = s.session_id "
                        + "where s.campaign_id = ?;"
                , campaignId);
        jdbcTemplate.update(
                "delete su from session_user su "
                + "inner join session s on su.session_id = s.session_id "
                + "where s.campaign_id = ?;"
                , campaignId);
        jdbcTemplate.update("delete from session where campaign_id = ?;", campaignId);
        jdbcTemplate.update("delete from campaign_user where campaign_id = ?;", campaignId);
        return jdbcTemplate.update("delete from campaign where campaign_id = ?;", campaignId) > 0;
    }

    private void addSessions(Campaign campaign) {
        final String sql = "select s.session_id, s.campaign_id, s.start_date, s.end_date "
                + "from session s "
                + "where s.campaign_id = ?;";
        var sessions = jdbcTemplate.query(sql, new SessionMapper(), campaign.getCampaignId());
        campaign.setSessionList(sessions);
    }

    private void addUsers(Campaign campaign) {
        final String sql = "select cu.campaign_id, cu.user_id, " +
                "u.user_id, u.username, u.city, u.state, u.`description`, u.password_hash, u.disabled, " +
                "c.campaign_id, c.user_id, c.name, c.description, c.type, " +
                "c.city, c.state, c.session_count, c.max_players, c.current_players " +
                "from campaign_user cu " +
                "inner join campaign c on cu.campaign_id = c.campaign_id " +
                "inner join user u on cu.user_id = u.user_id " +
                "where cu.campaign_id = ?;";

        var camapignUsers = jdbcTemplate.query(sql, new CampaignUserMapper(), campaign.getCampaignId());
        campaign.setUserList(camapignUsers);
    }
}