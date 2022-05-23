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
    private int session_id;

    private List<SessionUser> userList;

    @NonNull
    private Date startDate;

    @NonNull
    private Date endDate;
}
