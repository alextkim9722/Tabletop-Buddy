package capstone.data;

import capstone.models.Session;
import capstone.models.UserSchedule;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserScheduleRepository {
    UserSchedule create(UserSchedule session);

    boolean update(UserSchedule session);

    @Transactional
    boolean deleteById(int sessionId);

    UserSchedule getFromId(int id);
    List<UserSchedule> getFromUserId(int userId);

    boolean deleteBySessionId(int sessionId, int userId);
}
