package capstone.models;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
public class UserSchedule {
    @NonNull
    private int userScheduleId;

    @NonNull
    private int userId;

    private int sessionId;

    @NonNull
    private Timestamp startDate;

    @NonNull
    private Timestamp endDate;

    private Session session;
}
