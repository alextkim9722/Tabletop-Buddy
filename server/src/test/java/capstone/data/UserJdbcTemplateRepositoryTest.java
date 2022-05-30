package capstone.data;

import capstone.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserJdbcTemplateRepositoryTest {
    final static int NEXT_ID = 3;

    @Autowired
    UserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void init() {
        knownGoodState.set();
    }

    @Test
    @Order(1)
    void shouldFindBob() {
        User bob = repository.findByUsername("Bob");
        assertEquals("bob", bob.getUsername());
    }

    @Test
    @Order(2)
    void shouldFindBobCampaign() {
        User bob = repository.findByUsername("Bob");
        assertEquals("My DnD", bob.getHostedCampaignList().get(0).getName());
        assertEquals("My Other DnD", bob.getHostedCampaignList().get(1).getName());
    }

    @Test
    @Order(3)
    void shouldFindBobNotJoined() {
        User bob = repository.findByUsername("Bob");
        assertEquals(0, bob.getCampaignList().size());
    }

    @Test
    @Order(4)
    void shouldFindDaleJoinedCampaign() {
        User dale = repository.findByUsername("Dale");
        assertEquals("My DnD", dale.getCampaignList().get(0).getCampaign().getName());
        assertEquals("My Other DnD", dale.getCampaignList().get(1).getCampaign().getName());
    }

    @Test
    @Order(5)
    void shouldFindDaleJoinedSessions() {
        User dale = repository.findByUsername("Dale");
        assertEquals(Timestamp.valueOf("2003-04-05 12:00:00.000"), dale.getSessionList().get(3).getSession().getStartDate());
    }

    @Test
    @Order(7)
    void shouldAdd() {
        // all fields
        User user = makeUser();
        User actual = repository.create(user);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getUserId());
    }

    @Test
    @Order(8)
    void shouldUpdate() {
        User user = makeUser();
        user.setUsername("Porg");
        user.setUserId(2);
        repository.update(user);
        User actual = repository.findByUsername("Porg");
        assertEquals("Porg", actual.getUsername());
    }

    @Test
    @Order(6)
    void shouldFindBobSchedule() {
        User bob = repository.findByUsername("Bob");
        assertEquals(5, bob.getUserScheduleList().size());
        assertEquals(Timestamp.valueOf("2003-04-03 12:00:00.000"), bob.getUserScheduleList().get(2).getStartDate());
    }

    @Test
    @Order(9)
    void shouldDelete() {
        User user = makeUser();
        assertTrue(repository.deleteById(user.getUserId()));
    }

    @Test
    @Order(999)
    void reset(){
        KnownGoodState.hasRun = false;
    }

    private User makeUser() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Admin");
        User user = new User(NEXT_ID,"Blorb","pw",false,roles);
        user.setCity("Montgomery");
        user.setState("California");
        user.setDescription("Hello My Yellow");
        return user;
    }
}
