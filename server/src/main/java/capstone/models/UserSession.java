package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSession {

    @NonNull
    int userid;
    @NonNull
    Session session;
}
