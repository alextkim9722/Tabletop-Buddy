package capstone.data;

import capstone.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserJdbcTemplateRepositoryTest {
    final static int NEXT_ID = 3;

    @Autowired
    UserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindBob() {
        User bob = repository.findByUsername("Bob");
        assertEquals("bob", bob.getUsername());
    }

    @Test
    void shouldAdd() {
        // all fields
        User user = makeUser();
        User actual = repository.create(user);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getUserid());
    }

    @Test
    void shouldUpdate() {
        User user = makeUser();
        user.setUserid(2);
        assertEquals("Blorb", user.getUsername());
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
