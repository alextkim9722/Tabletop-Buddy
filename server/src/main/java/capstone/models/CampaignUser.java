package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class CampaignUser {

    @NonNull
    private int campaignid;
    @NonNull
    private User user;
}
