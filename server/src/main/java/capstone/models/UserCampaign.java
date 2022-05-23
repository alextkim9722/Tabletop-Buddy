package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserCampaign {

    @NonNull
    private int userid;
    @NonNull
    private Campaign campaign;
}
