package capstone.data;

import capstone.models.SessionUser;

public interface SessionUserRepository {
    boolean add(SessionUser su);
    boolean update(SessionUser cu);
    boolean delete(int sessionId, int userId);
}

