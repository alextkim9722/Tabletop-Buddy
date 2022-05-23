package capstone.data.mapper;

import capstone.models.CampaignUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CampaignUserMapper implements RowMapper<CampaignUser> {

    @Override
    public CampaignUser mapRow(ResultSet resultSet, int i) throws SQLException {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("NA");
        UserMapper usermapper = new UserMapper(roles);

        CampaignUser userSession = new CampaignUser(
                resultSet.getInt("campaign_id"),
                usermapper.mapRow(resultSet, i)
        );
        return userSession;
    }
}
