package capstone.data;

import capstone.models.Session;
import capstone.models.SessionUser;
import capstone.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SessionUserJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 6;

    @Autowired
    SessionUserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){ knownGoodState.set(); }

    @Test
    @Order(1)
    void shouldAdd(){
        SessionUser sessionUser = makeSessionUser();
        assertTrue(repository.add(sessionUser));
    }

    @Test
    @Order(2)
    void shouldDelete(){
        assertTrue(repository.delete(5, 2));
        assertFalse(repository.delete(5, 2));
    }

    @Test
    @Order(999)
    void reset(){
        KnownGoodState.hasRun = false;
    }

    SessionUser makeSessionUser(){
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Admin");
        User user = new User(1,"Blorb","pw",false,roles);
        user.setCity("Montgomery");
        user.setState("California");
        user.setDescription("Hello My Yellow");

        Session session = new Session(
                5,
                2,
                Date.valueOf("2003-04-10"),
                Date.valueOf("2003-04-11")
        );

        SessionUser sessionUser = new SessionUser(5, user);
        return sessionUser;
    }
}
