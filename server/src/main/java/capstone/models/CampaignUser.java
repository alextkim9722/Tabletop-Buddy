package capstone.models;

import lombok.Getter;
import lombok.Setter;

public class CampaignUser {

    @Getter
    @Setter
    int campaignid;

    @Getter
    @Setter
    User user;
}
