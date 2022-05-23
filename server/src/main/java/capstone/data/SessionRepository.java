package capstone.data;

import capstone.models.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SessionRepository {
    Session create(Session session);

    boolean update(Session session);

    @Transactional
    boolean deleteById(int session_id);

    List<Session> getFromUserId(int userid);

    List<Session> getFromCampaignId(int CampaignId);

}
