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
                resultSet.getString("user_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("type"),
                resultSet.getInt("session_count"),
                resultSet.getInt("max_players")
              //  resultSet.getInt("current_player")
        );

        campaign.setCity(resultSet.getString("city"));
        campaign.setState(resultSet.getString("state"));
        return campaign;
    }
}

