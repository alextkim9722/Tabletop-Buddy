package capstone.data;

import capstone.models.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SessionJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 6;

    @Autowired
    SessionJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){knownGoodState.set();}

    @Test
    void shouldCreate() {
    Session session = makeSession();
    Session actual = repository.create(session);
    assertNotNull(actual);
    assertEquals(NEXT_ID, actual.getSession_id());
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void getFromUserId() {
    }

    @Test
    void getFromCampaignId() {
    }

    private Session makeSession() {
        Session session = new Session(
                6,
                2,
                Date.valueOf("2003-04-10"),
                Date.valueOf("2003-04-11")
        );
        return session;
    }
}