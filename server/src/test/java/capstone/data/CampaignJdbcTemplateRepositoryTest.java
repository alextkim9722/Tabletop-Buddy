package capstone.data;

import capstone.models.Campaign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CampaignJdbcTemplateRepositoryTest {

    final static int NEXT_ID=3;

    @Autowired
    CampaignJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAll() {
        List<Campaign> campaigns = repository.findAll();
        assertNotNull(campaigns);

        assertTrue(campaigns.size() >=1 && campaigns.size() <=3);
    }

    @Test
    void shouldFindById() {
        Campaign myDnD = repository.findById(1);
        assertEquals(0, myDnD.getCampaignId());
        assertEquals("California", myDnD.getState());
        assertEquals(3, myDnD.getSessionCount());
    }


//    @Test
//    void shouldAdd() {
//        Campaign campaign = new Campaign(
//        .setName("test")
//        .setUserId(2)
//        .setDescription("testCampaign")
//        .setType("not DnD")
//        .setSessionCount(3)
//        .setMaxPlayers(6);
//        );
//
//        campaign.setCity("Milwaukee");
//        campaign.setState("WI");
//    }

    @Test
    void shouldUpdate() {
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(1));  //TODO [TEST FAILING]
        assertFalse(repository.deleteById(1));
    }

}