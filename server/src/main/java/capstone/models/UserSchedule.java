package capstone.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
public class UserSchedule {
    @Setter(AccessLevel.NONE)
    private int user_schedule_id;

    private List<Session> sessionList;

    @NonNull
    private Date startDate;

    @NonNull
    private Date endDate;
}
