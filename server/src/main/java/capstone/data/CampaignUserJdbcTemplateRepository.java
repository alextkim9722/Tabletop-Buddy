package capstone.data;

import capstone.models.CampaignUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CampaignUserJdbcTemplateRepository implements  CampaignUserRepository{

    private final JdbcTemplate jdbcTemplate;

    public CampaignUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(CampaignUser cu) {
        final String sql = "insert into campaign_user (campaign_id, user_id) values "
                + "(?,?);";

        return jdbcTemplate.update(sql,
                cu.getCampaignId(),
                cu.getUser().getUserId()) > 0;
    }

    @Override
    public boolean update(CampaignUser cu) {
        final String sql = "update campaign_user set "
                + "campaign_id = ?,"
                + "user_id = ?,"
                + "where campaign_id = ? and user_id = ?;";

        return jdbcTemplate.update(sql,
                cu.getCampaignId(),
                cu.getUser().getUserId(),
                cu.getCampaignId(),
                cu.getUser().getUserId()) > 0;
    }

    @Override
    public boolean delete(int campaignId, int userId) {
        final String sql = "delete from campaign_user "
                + "where campaign_id = ? and user_id = ?;";

        return jdbcTemplate.update(sql, campaignId, userId) > 0;
    }
}
