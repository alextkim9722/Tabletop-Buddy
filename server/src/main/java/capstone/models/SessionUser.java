package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class SessionUser {

    @NonNull
    private int sessionid;
    @NonNull
    private User user;
}
