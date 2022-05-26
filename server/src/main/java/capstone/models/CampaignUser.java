package capstone.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
public class CampaignUser {

    private int campaignId;
    private User user;

    public CampaignUser(int campaignId, User user) {
        this.campaignId = campaignId;
        this.user = user;
    }

    public CampaignUser(){}
}
