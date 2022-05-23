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
    int user_schedule_id;

    List<Session> sessionList;

    @NonNull
    Date startDate;

    @NonNull
    Date endDate;
}
