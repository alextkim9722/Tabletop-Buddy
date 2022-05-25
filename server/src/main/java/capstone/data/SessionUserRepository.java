package capstone.data;

import capstone.models.SessionUser;

public interface SessionUserRepository {
    boolean add(SessionUser su);
    boolean delete(int sessionId, int userId);
}

