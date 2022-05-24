package capstone.models;

import lombok.Data;
import lombok.NonNull;

import java.sql.Date;
import java.util.List;

@Data
public class Session {
        @NonNull
        private int sessionid;

        @NonNull
        private int CampaignId;

        private List<SessionUser> userList;

        @NonNull
        private Date startDate;

        @NonNull
        private Date endDate;
}
