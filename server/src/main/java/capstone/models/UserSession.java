package capstone.models;

import lombok.Getter;
import lombok.Setter;

public class UserSession {

    @Getter
    @Setter
    int userid;

    @Getter
    @Setter
    Session session;
}
