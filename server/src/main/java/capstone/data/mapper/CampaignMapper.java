package capstone.data.mapper;

import capstone.models.Campaign;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CampaignMapper implements RowMapper <Campaign> {

    @Override
    public Campaign mapRow(ResultSet resultSet, int i) throws SQLException {
        Campaign campaign = new Campaign(
                resultSet.getInt("campaign_id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getString("type"),
                resultSet.getString("city"),
                resultSet.getString("state"),
                resultSet.getInt("max_players")
        );

        campaign.setSessionCount(resultSet.getInt("session_count"));
        campaign.setDescription(resultSet.getString("description"));
        campaign.setCurrentPlayers(resultSet.getInt("current_players"));
        return campaign;
    }
}

