package capstone.service;

import capstone.data.CampaignRepository;
import capstone.models.Campaign;
import capstone.models.Filter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class CampaignServiceTest {

    @Autowired
    CampaignService campaignService;

    @MockBean
    CampaignRepository campaignRepository;

    @Test
    void shouldAddWhenValid() {
        Campaign expected = makeCampaign();
        Campaign arg = makeCampaign();
        arg.setCampaignId(0);

        when(campaignRepository.add(arg)).thenReturn(expected);
        Result<Campaign> result = campaignService.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddWhenMaxPlayersLessThanZero() {
        Campaign campaign = makeCampaign();
        campaign.setCampaignId(0);
        campaign.setMaxPlayers(-1);
        Result<Campaign> result = campaignService.add(campaign);
        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().get(0), "Maximum players must be greater than 0");
    }

    @Test
    void shouldNotAddWhenCurrentPlayersExceedsMaxPlayers() {
        Campaign campaign = makeCampaign();
        campaign.setCampaignId(0);
        campaign.setCurrentPlayers(100);
        Result<Campaign> result = campaignService.add(campaign);
        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().get(0), "You cannot exceed the maximum amount of players");
    }

    @Test
    void shouldNotAddWhenCampaignIdIsSet() {
        Campaign campaign = makeCampaign();
        Result<Campaign> result = campaignService.add(campaign);
        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().get(0), "campaignId cannot be set for `add` operation");
    }

    @Test
    void shouldUpdateWhenValid() {
        Campaign campaign = makeCampaign();

        when(campaignRepository.update(campaign)).thenReturn(true);
        Result<Campaign> result = campaignService.update(campaign);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateWhenMaxPlayersLessThanZero() {
        Campaign campaign = makeCampaign();
        campaign.setMaxPlayers(-1);

        Result<Campaign> result = campaignService.update(campaign);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenCurrentPlayersExceedsMaxPlayers() {
        Campaign campaign = makeCampaign();
        campaign.setCurrentPlayers(100);

        Result<Campaign> result = campaignService.update(campaign);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenCampaignIdIsNotSet() {
        Campaign campaign = makeCampaign();
        campaign.setCampaignId(0);

        Result<Campaign> result = campaignService.update(campaign);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenCampaignIsNotFound() {
        Campaign campaign = makeCampaign();
        campaign.setCampaignId(50000);

        when(campaignRepository.update(campaign)).thenReturn(false);
        Result<Campaign> result = campaignService.update(campaign);
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    @Test
    void shouldFindCampaignWithStartFilter(){
        Campaign campaign = makeCampaign();
        List<Campaign> campaignList = new ArrayList<>();
        campaignList.add(campaign);
        Filter filter = new Filter();
        filter.setType(null);
        filter.setPlayers(-1);
        filter.setSize(-1);
        filter.setStart(Timestamp.valueOf("2023-04-10 12:00:00.000"));
        when(campaignRepository.findByTag(null, -1, -1, Timestamp.valueOf("2023-04-10 12:00:00.000"))).thenReturn(campaignList);
        List<Campaign> actual = campaignService.findByTag(filter);
        assertEquals("Warhammer 40k Pros", actual.get(0).getName());
    }

    Campaign makeCampaign() {
        Campaign campaign = new Campaign(
          1,
          1,
          "Warhammer 40k Pros",
          "Warhammer 40k",
          "Milwaukee",
          "Wisconsin",
          5
        );
        return campaign;
    }
}