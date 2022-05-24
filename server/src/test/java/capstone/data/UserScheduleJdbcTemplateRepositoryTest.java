package capstone.data;

import capstone.models.UserSchedule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserScheduleJdbcTemplateRepositoryTest {

    @Autowired
    UserScheduleJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){ knownGoodState.set(); }

    @Test
    @Order(1)
    void shouldCreate() {
        UserSchedule session = makeUserSchedule();
        UserSchedule actual = repository.create(session);
        assertNotNull(actual);
        assertEquals(6, actual.getUserScheduleid());
    }

    @Test
    @Order(2)
    void update() {
        UserSchedule session = makeUserSchedule();
        session.setStartDate(Timestamp.valueOf("2003-04-12 12:00:00.000"));
        repository.update(session);
        List<UserSchedule> sessionList = repository.getFromUserId(1);
        assertEquals(Timestamp.valueOf("2003-04-12 12:00:00.000"), sessionList.get(5).getStartDate());
    }

    @Test
    @Order(3)
    void deleteById() {
        assertTrue(repository.deleteById(1));
        assertFalse(repository.deleteById(1));
    }

    @Test
    @Order(999)
    void reset(){
        KnownGoodState.hasRun = false;
    }

    UserSchedule makeUserSchedule(){
        UserSchedule userSchedule = new UserSchedule(
                6,
                1,
                Timestamp.valueOf("2003-04-10 12:00:00.000"),
                Timestamp.valueOf("2003-04-11 12:00:00.000")
        );

        userSchedule.setSessionid(1);

        return userSchedule;
    }
}
