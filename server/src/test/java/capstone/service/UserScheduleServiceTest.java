package capstone.service;

import capstone.data.SessionRepository;
import capstone.data.UserScheduleRepository;
import capstone.models.Campaign;
import capstone.models.UserSchedule;
import capstone.models.UserSchedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserScheduleServiceTest {

    @Autowired
    UserScheduleService service;

    @MockBean
    UserScheduleRepository repository;

    @Test
    void shouldFindFromUser() {
        UserSchedule userSchedule = makeUserSchedule();
        List<UserSchedule> expected = new ArrayList<>();
        expected.add(userSchedule);
        when(repository.getFromUserId(1)).thenReturn(expected);
        List<UserSchedule> actual = service.getFromUserId(1);
        assertEquals(expected.get(0).getStartDate(), actual.get(0).getStartDate());
    }

    /*
    Lombok already fills this issue.
    @Test
    void shouldNotAddWhenDatesAreNull() {
        UserSchedule userSchedule = makeUserSchedule();
        userSchedule.setStartDate(null);
        Result<UserSchedule> result = service.create(userSchedule);
        assertEquals(ResultType.INVALID, result.getType());

        userSchedule.setEndDate(null);
        result = service.create(userSchedule);
        assertEquals(ResultType.INVALID, result.getType());
    }
     */

    @Test
    void shouldAddWhenValid() {
        UserSchedule userSchedule = makeUserSchedule();
        userSchedule.setUserScheduleId(0);
        Result<UserSchedule> result = service.create(userSchedule);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotAddWhenStartisNotBeforeEnd() {
        UserSchedule userSchedule = makeUserSchedule();
        userSchedule.setStartDate(Timestamp.valueOf("2023-04-11 10:00:00.000"));
        userSchedule.setEndDate(Timestamp.valueOf("2023-04-11 8:00:00.000"));
        Result<UserSchedule> result = service.create(userSchedule);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenOverlapOtherSchedules() {
        UserSchedule userSchedule = makeUserSchedule();

        // For the start date
        UserSchedule userScheduleA = makeUserSchedule();
        userScheduleA.setStartDate(Timestamp.valueOf("2023-04-10 11:00:00.000"));
        userScheduleA.setEndDate(Timestamp.valueOf("2023-04-10 13:00:00.000"));

        // For the end date
        UserSchedule userScheduleB = makeUserSchedule();
        userScheduleB.setStartDate(Timestamp.valueOf("2023-04-11 11:00:00.000"));
        userScheduleB.setEndDate(Timestamp.valueOf("2023-04-11 13:00:00.000"));

        List<UserSchedule> userSchedulesList = new ArrayList<>();
        userSchedulesList.add(userScheduleA);
        userSchedulesList.add(userScheduleB);

        Result<UserSchedule> result = service.create(userSchedule);
        when(repository.getFromUserId(1)).thenReturn(userSchedulesList);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenNotInFuture() {
        UserSchedule userSchedule = makeUserSchedule();
        userSchedule.setStartDate(Timestamp.valueOf("2003-04-11 10:00:00.000"));
        userSchedule.setEndDate(Timestamp.valueOf("2003-04-11 8:00:00.000"));
        Result<UserSchedule> result = service.create(userSchedule);
        assertEquals(ResultType.INVALID, result.getType());
    }

    UserSchedule makeUserSchedule(){
        UserSchedule userSchedule = new UserSchedule(
                6,
                1,
                Timestamp.valueOf("2023-04-10 12:00:00.000"),
                Timestamp.valueOf("2023-04-11 12:00:00.000")
        );

        userSchedule.setSessionId(1);

        return userSchedule;
    }
}
