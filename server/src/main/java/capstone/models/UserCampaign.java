package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserCampaign {

    @NonNull
    int userid;
    @NonNull
    Campaign campaign;
}
