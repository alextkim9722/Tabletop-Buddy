package capstone.data;

import capstone.models.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SessionRepository {

    Session create(Session session);

    boolean update(Session session);

    @Transactional
    boolean deleteById(int sessionId);

    List<Session> getFromUserId(int userId);

    List<Session> getFromCampaignId(int CampaignId);
}
