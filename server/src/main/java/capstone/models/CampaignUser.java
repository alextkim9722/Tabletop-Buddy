package capstone.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
public class CampaignUser {

    private int campaignid;
    private User user;

    public CampaignUser(int campaignid, User user) {
        this.campaignid = campaignid;
        this.user = user;
    }

    public CampaignUser(){}
}
