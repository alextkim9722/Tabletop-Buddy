package capstone.service;

import capstone.data.CampaignRepository;
import capstone.data.UserRepository;
import capstone.models.Campaign;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<Campaign> findAll() { return campaignRepository.findAll(); }

    public Campaign findById(int campaignId) { return campaignRepository.findById(campaignId); }

    public List<Campaign> findbyTag(String type, int players, int size, Timestamp start) {
        return campaignRepository.findbyTag(type, players, size, start); }

    public boolean deleteById(int campaignId) { return campaignRepository.deleteById(campaignId); }

    public Result<Campaign> add(Campaign campaign) {
        Result<Campaign> result = validate(campaign);
        if (!result.isSuccess()) {
            return result;
        }

        if (campaign.getCampaignId() != 0) {
            result.addMessage("campaignId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        campaign = campaignRepository.add(campaign);
        result.setPayload(campaign);
        return result;
    }

    public Result<Campaign> update(Campaign campaign) {
        Result<Campaign> result = validate(campaign);
        if (!result.isSuccess()) {
            return result;
        }

        if (campaign.getCampaignId() <= 0) {
            result.addMessage("campaignId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!campaignRepository.update(campaign)) {
            String message = String.format("campaignId: %s, not found", campaign.getCampaignId());
            result.addMessage(message, ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<Campaign> validate(Campaign campaign) {
        Result<Campaign> result = new Result<>();
        if (campaign == null) {
            result.addMessage("Campaign cannot be null", ResultType.INVALID);
            return result;
        }
        if (campaign.getMaxPlayers() <= 0) {
            result.addMessage("Maximum players must be greater than 0", ResultType.INVALID);
        }
        if (campaign.getCurrentPlayers() > campaign.getMaxPlayers()) {
            result.addMessage("You cannot exceed the maximum amount of players", ResultType.INVALID);
        }

        return result;

    }
}
