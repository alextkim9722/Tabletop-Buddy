package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class CampaignUser {

    @NonNull
    int campaignid;
    @NonNull
    User user;
}
