package capstone.data;

import capstone.models.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CampaignUserJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 6;

    @Autowired
    CampaignUserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){ knownGoodState.set(); }

    @Test
    @Order(1)
    void shouldAdd(){
        CampaignUser campaignUser = makeCampaignUser();
        assertTrue(repository.add(campaignUser));
    }

    @Test
    @Order(2)
    void shouldDelete(){
        assertTrue(repository.delete(1, 2));
        assertFalse(repository.delete(1, 2));
    }

    @Test
    @Order(999)
    void reset(){
        KnownGoodState.hasRun = false;
    }

    CampaignUser makeCampaignUser(){
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Admin");
        User user = new User(1,"Blorb","pw",false,roles);
        user.setCity("Montgomery");
        user.setState("California");
        user.setDescription("Hello My Yellow");

        Campaign campaign = new Campaign(
                NEXT_ID,
                1,
                "Blob",
                "Blarg",
                "Darg",
                "Targ",
                10
        );

        CampaignUser campaignUser = new CampaignUser(1, user);
        return campaignUser;
    }
}
