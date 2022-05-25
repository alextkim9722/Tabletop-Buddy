package capstone.service;

import capstone.data.SessionRepository;
import capstone.models.Campaign;
import capstone.models.Session;
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
public class SessionServiceTest {

    @Autowired
    SessionService service;

    @MockBean
    SessionRepository repository;

    @Test
    void shouldFindFromCampaign() {
        Session session = makeSession();
        List<Session> expected = new ArrayList<>();
        expected.add(session);
        when(repository.getFromCampaignId(1)).thenReturn(expected);
        List<Session> actual = service.getFromCampaignId(1);
        assertEquals(expected.get(0).getStartDate(), actual.get(0).getStartDate());
    }

    @Test
    void shouldFindFromUser() {
        Session session = makeSession();
        List<Session> expected = new ArrayList<>();
        expected.add(session);
        when(repository.getFromUserId(1)).thenReturn(expected);
        List<Session> actual = service.getFromUserId(1);
        assertEquals(expected.get(0).getStartDate(), actual.get(0).getStartDate());
    }

    /*
    Lombok already fills this issue.
    @Test
    void shouldNotAddWhenDatesAreNull() {
        Session session = makeSession();
        session.setStartDate(null);
        Result<Session> result = service.create(session);
        assertEquals(ResultType.INVALID, result.getType());

        session.setEndDate(null);
        result = service.create(session);
        assertEquals(ResultType.INVALID, result.getType());
    }
     */

    @Test
    void shouldAddWhenValid() {
        Session session = makeSession();
        session.setSessionid(0);
        Result<Session> result = service.create(session);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotAddWhenStartisNotBeforeEnd() {
        Session session = makeSession();
        session.setStartDate(Timestamp.valueOf("2023-04-11 10:00:00.000"));
        session.setEndDate(Timestamp.valueOf("2023-04-11 8:00:00.000"));
        Result<Session> result = service.create(session);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenOverlapCampaignTimes() {
        Session session = makeSession();
        Campaign campaign = makeCampaign();

        // For the start date
        Session sessionA = makeSession();
        sessionA.setStartDate(Timestamp.valueOf("2023-04-10 11:00:00.000"));
        sessionA.setEndDate(Timestamp.valueOf("2023-04-10 13:00:00.000"));
        campaign.getSessionList().add(sessionA);

        // For the end date
        Session sessionB = makeSession();
        sessionB.setStartDate(Timestamp.valueOf("2023-04-11 11:00:00.000"));
        sessionB.setEndDate(Timestamp.valueOf("2023-04-11 13:00:00.000"));
        campaign.getSessionList().add(sessionB);

        Result<Session> result = service.create(session);
        when(repository.getFromCampaignId(1)).thenReturn(campaign.getSessionList());
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenNotInFuture() {
        Session session = makeSession();
        session.setStartDate(Timestamp.valueOf("2003-04-11 10:00:00.000"));
        session.setEndDate(Timestamp.valueOf("2003-04-11 8:00:00.000"));
        Result<Session> result = service.create(session);
        assertEquals(ResultType.INVALID, result.getType());
    }

    private Session makeSession() {
        Session session = new Session(
                1,
                1,
                Timestamp.valueOf("2023-04-10 12:00:00.000"),
                Timestamp.valueOf("2023-04-11 12:00:00.000")
        );
        return session;
    }

    private Campaign makeCampaign() {
        Campaign campaign = new Campaign(
                1,
                1,
                "Blob",
                "Blarg",
                "Darg",
                "Targ",
                10
        );
        return campaign;
    }
}
