package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class SessionUser {

    @NonNull
    int sessionid;
    @NonNull
    User user;
}
