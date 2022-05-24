package capstone.data;

import capstone.data.mapper.CampaignMapper;
import capstone.models.Campaign;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
                "city, state, session_count, max_players from campaign limit 1000;"; //[TODO Current player?]
        return jdbcTemplate.query(sql,new CampaignMapper());
    }

    @Override
    public Campaign findById(int campaignId) {
        final String sql = "select campaign_id, user_id, name, description, type, " +
                "city, state, session_count, max_players "
                + "from campaign "
                + "where campaign_id = ?;";

        Campaign result = jdbcTemplate.query(sql, new CampaignMapper(), campaignId).stream()
                .findAny().orElse(null);


       return result;
    }


    @Override
    public Campaign add(Campaign campaign) {
        final String sql = "insert into campaign (name, description, type, " +
                "city, state, session_count, max_players) values (?,?,?,?,?,?,?);";

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
                + "max_players = ?, "
                + "where campaign_id = ?;";

        return jdbcTemplate.update(sql,
                campaign.getName(),
                campaign.getDescription(),
                campaign.getType(),
                campaign.getCity(),
                campaign.getState(),
                campaign.getSessionCount(),
                campaign.getMaxPlayers()) >0;
    }

    @Override
    @Transactional
    public boolean deleteById(int campaignId) {
        jdbcTemplate.update("delete from session where campaign_id = ?;", campaignId);
        return jdbcTemplate.update("delete from campaign where campaign = ?;", campaignId) > 0;
    }

}