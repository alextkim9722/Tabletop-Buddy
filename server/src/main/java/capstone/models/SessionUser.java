package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class SessionUser {

    @NonNull
    private int sessionId;
    @NonNull
    private User user;
}
