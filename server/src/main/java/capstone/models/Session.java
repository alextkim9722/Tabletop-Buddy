package capstone.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
public class Session {
    @Setter(AccessLevel.NONE)
    int session_id;

    List<SessionUser> userList;

    @NonNull
    Date startDate;

    @NonNull
    Date endDate;
}
