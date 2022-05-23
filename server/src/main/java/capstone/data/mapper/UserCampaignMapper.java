package capstone.data.mapper;

import capstone.models.UserCampaign;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserCampaignMapper implements RowMapper<UserCampaign> {

    @Override
    public UserCampaign mapRow(ResultSet resultSet, int i) throws SQLException {
        /*
        CampaignMapper campaignMapper = new CampaignMapper();

        UserCampaign userCampaign = new UserCampaign(
                resultSet.getInt("user_id"),
                campaignMapper.mapRow(resultSet, i)
        );
        return userCampaign;
         */

        return null;
    }
}
