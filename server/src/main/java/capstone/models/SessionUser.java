package capstone.models;

import lombok.Getter;
import lombok.Setter;

public class SessionUser {

    @Getter
    @Setter
    int sessionid;

    @Getter
    @Setter
    User user;
}
