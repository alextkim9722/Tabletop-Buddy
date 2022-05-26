package capstone.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSession {

    @NonNull
    private int userId;
    @NonNull
    private Session session;
}
