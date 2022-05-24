package capstone.data;

import capstone.models.Campaign;
import capstone.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CampaignJdbcTemplateRepositoryTest {

    final static int NEXT_ID=3;

    @Autowired
    CampaignJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    @Order(1)
    void shouldFindAll() {
        List<Campaign> campaigns = repository.findAll();
        assertNotNull(campaigns);

        assertTrue(campaigns.size() >=1 && campaigns.size() <=3);
    }

    @Test
    @Order(2)
    void shouldFindById() {
        Campaign myDnD = repository.findById(1);
        assertEquals(1, myDnD.getCampaignId());
        assertEquals("California", myDnD.getState());
        assertEquals(3, myDnD.getSessionCount());
    }

    @Test
    @Order(5)
    void shouldAdd(){
        Campaign campaign = makeCampaign();
        repository.add(campaign);
        assertEquals(3, repository.findAll().size());
    }

    @Test
    @Order(6)
    void shouldFindUsername(){
        Campaign myDnD = repository.findById(1);
        assertEquals("dale", myDnD.getUserList().get(0).getUser().getUsername());
    }

    @Test
    @Order(7)
    void shouldFindSessionDate(){
        Campaign myDnD = repository.findById(1);
        assertEquals(Date.valueOf("2003-04-03"), myDnD.getSessionList().get(2).getStartDate());
    }

    @Test
    @Order(3)
    void shouldFindByPlayerCount() {
        List<Campaign> campaignList = repository.findbyTag(null, 0, -1, null);
        assertEquals(2, campaignList.size());
        assertEquals("My DnD", campaignList.get(0).getName());
    }

    @Test
    @Order(4)
    void shouldFindByDateAndType() {
        List<Campaign> campaignList = repository.findbyTag("DnD", -1, -1, Date.valueOf("2003-03-10"));
        assertEquals(1, campaignList.size());
        assertEquals("My DnD", campaignList.get(0).getName());
    }

    private Campaign makeCampaign() {
        Campaign campaign = new Campaign(
                NEXT_ID,
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