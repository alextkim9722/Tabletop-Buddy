package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSession {

    @NonNull
    private int userid;
    @NonNull
    private Session session;
}
