package capstone.service;

import capstone.data.SessionRepository;
import capstone.data.SessionUserRepository;
import capstone.data.UserScheduleRepository;
import capstone.models.Session;
import capstone.models.SessionUser;
import capstone.models.User;
import capstone.models.UserSchedule;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository repository;
    private final UserScheduleRepository userRepository;
    private final SessionUserRepository sessionUserRepository;

    public SessionService(SessionRepository repository, UserScheduleRepository userRepository, SessionUserRepository sessionUserRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.sessionUserRepository = sessionUserRepository;
    }

    public Result<Session> create(Session session) {
        Result<Session> result = validate(session);
        if (!result.isSuccess()) {
            return result;
        }

        if (session.getSessionId() != 0) {
            result.addMessage("session_id cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        session = repository.create(session);
        result.setPayload(session);
        return result;
    }

    public Result<Session> update(Session session) {
        Result<Session> result = validate(session);
        if (!result.isSuccess()) {
            return result;
        }

        if (session.getSessionId() <= 0) {
            result.addMessage("session_id must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(session)) {
            String msg = String.format("session_id: %s, not found", session.getSessionId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean delete(int id) { return repository.deleteById(id); }

    public List<Session> getFromUserId(int id) { return repository.getFromUserId(id); }

    public List<Session> getFromCampaignId(int id) { return repository.getFromCampaignId(id); }

    public Result<Void> createUser(SessionUser su) {
        Result<Void> result = validate(su);
        if (!result.isSuccess()) {
            return result;
        }

        if (!sessionUserRepository.add(su)) {
            result.addMessage("user not added", ResultType.INVALID);
        }

        return result;
    }

    public boolean deleteUserByKey(int sessionId, int userId) {
        return sessionUserRepository.delete(sessionId, userId);
    }

    private Result<Session> validate(Session session) {
        Result<Session> result = new Result<>();
        if (session == null) {
            result.addMessage("session cannot be null", ResultType.INVALID);
            return result;
        }

        /*
        if (session.getStartDate() == null) {
            result.addMessage("start date is required", ResultType.INVALID);
        }

        if (session.getEndDate() == null) {
            result.addMessage("end date is required", ResultType.INVALID);
        }
         */

        if (session.getStartDate().after(session.getEndDate())) {
            result.addMessage("start date should be after the end date", ResultType.INVALID);
        }

        List<Session> sessionList = repository.getFromCampaignId(session.getCampaignId());
        for(Session s : sessionList) {
            if(s.getSessionId() != session.getSessionId()) {
                if (session.getStartDate().before(s.getEndDate()) && session.getStartDate().after(s.getStartDate())
                        || session.getEndDate().before(s.getEndDate()) && session.getEndDate().after(s.getStartDate())) {
                    result.addMessage("An overlap has been detected", ResultType.INVALID);
                }
            }

            List<SessionUser> userList = s.getUserList();

            if(userList != null && !userList.isEmpty()) {
                for (SessionUser su : userList) {
                    List<UserSchedule> userSchedulesList = su.getUser().getUserScheduleList();

                    if(userSchedulesList != null && !userSchedulesList.isEmpty()) {
                        for (UserSchedule us : userSchedulesList) {
                            if (session.getStartDate().before(us.getEndDate()) && session.getStartDate().after(us.getStartDate())
                                    || session.getEndDate().before(us.getEndDate()) && session.getEndDate().after(us.getStartDate())) {
                                result.addMessage("An overlap has been detected with user " + su.getUser().getUsername(), ResultType.INVALID);
                            }
                        }
                    }
                }
            }
        }

        if (session.getStartDate().before(Timestamp.from(Instant.now()))){
            result.addMessage("Start must be in the future", ResultType.INVALID);
        }

        return result;
    }

    private Result<Void> validate(SessionUser su) {
        Result<Void> result = new Result<>();
        if (su == null) {
            result.addMessage("session-user cannot be null", ResultType.INVALID);
            return result;
        }

        if(su.getUser() == null) {
            result.addMessage("user cannot be null", ResultType.INVALID);
        }

        return result;
    }
}
