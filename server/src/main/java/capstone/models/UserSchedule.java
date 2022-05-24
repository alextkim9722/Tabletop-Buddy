package capstone.models;

import lombok.Data;
import lombok.NonNull;

import java.sql.Date;

@Data
public class UserSchedule {
    @NonNull
    private int userScheduleid;

    @NonNull
    private int userid;

    private int sessionid;

    @NonNull
    private Date startDate;

    @NonNull
    private Date endDate;

    private Session session;
}
