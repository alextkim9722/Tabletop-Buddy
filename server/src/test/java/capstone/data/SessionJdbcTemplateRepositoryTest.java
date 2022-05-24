package capstone.data;

import capstone.models.Session;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SessionJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 6;

    @Autowired
    SessionJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){ knownGoodState.set(); }

    @Test
    @Order(4)
    void shouldCreate() {
        Session session = makeSession();
        Session actual = repository.create(session);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getSessionid());
    }

    @Test
    @Order(5)
    void update() {
        Session session = makeSession();
        session.setStartDate(Date.valueOf("2003-04-12"));
        session.setSessionid(2);
        repository.update(session);
        List<Session> sessionList = repository.getFromUserId(2);
        assertEquals(Date.valueOf("2003-04-12"), sessionList.get(1).getStartDate());
    }

    @Test
    @Order(6)
    void deleteById() {
        repository.deleteById(1);
        List<Session> sessionList = repository.getFromUserId(2);
        assertEquals(4, sessionList.size());
    }

    @Test
    @Order(1)
    void getFromUserId() {
        List<Session> sessionList = repository.getFromUserId(2);
        assertEquals(5, sessionList.size());
        assertEquals(Date.valueOf("2003-04-05"), sessionList.get(3).getStartDate());
    }

    @Test
    @Order(2)
    void getFromCampaignId() {
        List<Session> sessionList = repository.getFromCampaignId(2);
        assertEquals(2, sessionList.size());
        assertEquals(Date.valueOf("2003-04-05"), sessionList.get(0).getStartDate());
    }

    @Test
    @Order(3)
    void getUserNameFromCampaignId() {
        List<Session> sessionList = repository.getFromCampaignId(2);
        assertEquals(2, sessionList.size());
        assertEquals("dale", sessionList.get(0).getUserList().get(0).getUser().getUsername());
    }

    private Session makeSession() {
        Session session = new Session(
            NEXT_ID,
            2,
            Date.valueOf("2003-04-10"),
            Date.valueOf("2003-04-11")
        );
        return session;
    }
}