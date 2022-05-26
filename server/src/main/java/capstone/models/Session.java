package capstone.models;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Session {
        @NonNull
        private int sessionId;

        @NonNull
        private int CampaignId;

        private List<SessionUser> userList;

        @NonNull
        private Timestamp startDate;

        @NonNull
        private Timestamp endDate;
}
