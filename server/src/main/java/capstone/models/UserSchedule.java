package capstone.models;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
public class UserSchedule {
    @NonNull
    private int userScheduleid;

    @NonNull
    private int userid;

    private int sessionid;

    @NonNull
    private Timestamp startDate;

    @NonNull
    private Timestamp endDate;

    private Session session;
}
