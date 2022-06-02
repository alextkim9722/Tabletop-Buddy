package capstone.service;

import capstone.data.CampaignUserRepository;
import capstone.data.SessionRepository;
import capstone.data.UserRepository;
import capstone.data.UserScheduleRepository;
import capstone.models.CampaignUser;
import capstone.models.UserCampaign;
import capstone.models.UserSchedule;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class UserScheduleService {
    private final UserScheduleRepository repository;

    public UserScheduleService(UserScheduleRepository repository) {
        this.repository = repository;
    }

    public Result<UserSchedule> create(UserSchedule userSchedule) {
        Result<UserSchedule> result = validate(userSchedule);
        if (!result.isSuccess()) {
            return result;
        }

        if (userSchedule.getUserScheduleId() != 0) {
            result.addMessage("userScheduleId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        userSchedule = repository.create(userSchedule);
        result.setPayload(userSchedule);
        return result;
    }

    public Result<UserSchedule> update(UserSchedule userSchedule) {
        Result<UserSchedule> result = validate(userSchedule);
        if (!result.isSuccess()) {
            return result;
        }

        if (userSchedule.getUserScheduleId() <= 0) {
            result.addMessage("userScheduleId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(userSchedule)) {
            String msg = String.format("userScheduleId: %s, not found", userSchedule.getSessionId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean delete(int id) { return repository.deleteById(id); }

    public boolean delete(int sessionId, int userId) {return repository.deleteBySessionId(sessionId,userId);}

    public List<UserSchedule> getFromUserId(int id) { return repository.getFromUserId(id); }
    public UserSchedule getFromId(int id) { return repository.getFromId(id); }

    private Result<UserSchedule> validate(UserSchedule userSchedule) {
        Result<UserSchedule> result = new Result<>();
        if (userSchedule == null) {
            result.addMessage("UserSchedule cannot be null", ResultType.INVALID);
            return result;
        }

        /*
        if (UserSchedule.getStartDate() == null) {
            result.addMessage("start date is required", ResultType.INVALID);
        }

        if (UserSchedule.getEndDate() == null) {
            result.addMessage("end date is required", ResultType.INVALID);
        }
         */

        if (userSchedule.getStartDate().after(userSchedule.getEndDate())) {
            result.addMessage("start date should be after the end date", ResultType.INVALID);
        }

        List<UserSchedule> userList = getFromUserId(userSchedule.getUserId());

        if(userList != null && !userList.isEmpty()) {
            for (UserSchedule us : userList) {
                if(us.getUserScheduleId() != userSchedule.getUserScheduleId()) {
                    if (userSchedule.getStartDate().before(us.getEndDate()) && userSchedule.getStartDate().after(us.getStartDate())
                            || userSchedule.getEndDate().before(us.getEndDate()) && userSchedule.getEndDate().after(us.getStartDate())
                            || userSchedule.getEndDate().equals(us.getEndDate()) || userSchedule.getStartDate().equals(us.getStartDate())
                            || userSchedule.getStartDate().equals(us.getEndDate()) || userSchedule.getEndDate().equals(us.getStartDate())) {
                        result.addMessage("An overlap has been detected", ResultType.INVALID);
                    }
                }
            }
        }

        if (userSchedule.getStartDate().before(Timestamp.from(Instant.now()))){
            result.addMessage("Start must be in the future", ResultType.INVALID);
        }

        return result;
    }
}
