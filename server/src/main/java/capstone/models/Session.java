package capstone.models;


import lombok.Data;
import lombok.NonNull;


import java.sql.Date;
import java.util.List;

@Data
public class Session {
    @NonNull
    private int session_id;

    @NonNull
    private int CampaignId;

    private List<SessionUser> userList;

    @NonNull
    private Date startDate;

    @NonNull
    private Date endDate;

    public Session(int session_id, int campaignId, Date start_date, Date end_date) {
        this.session_id = session_id;
        this.CampaignId = campaignId;
        this.startDate = start_date;
        this.endDate = end_date;
    }
}
