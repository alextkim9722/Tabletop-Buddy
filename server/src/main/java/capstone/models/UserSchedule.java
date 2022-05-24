package capstone.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
public class UserSchedule {
    @NonNull
    private int userScheduleid;

    @NonNull
    private Date startDate;

    @NonNull
    private Date endDate;

    private Session session;
}
