package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserCampaign {

    @NonNull
    private int userId;
    @NonNull
    private Campaign campaign;
}
