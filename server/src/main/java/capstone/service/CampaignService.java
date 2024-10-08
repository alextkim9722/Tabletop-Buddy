package capstone.service;

import capstone.data.CampaignRepository;
import capstone.data.CampaignUserRepository;
import capstone.models.Campaign;
import capstone.models.CampaignUser;
import capstone.models.Filter;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignUserRepository campaignUserRepository;

    public CampaignService(CampaignRepository campaignRepository, CampaignUserRepository campaignUserRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignUserRepository = campaignUserRepository;
    }

    public List<Campaign> findAll() { return campaignRepository.findAll(); }

    public Campaign findById(int campaignId) { return campaignRepository.findById(campaignId); }

    public List<Campaign> findByTag(Filter filter) {
        return campaignRepository.findByTag(filter.getType(), filter.getPlayers(), filter.getSize(), filter.getStart()); }

    public Result<Boolean> deleteById(Campaign campaign) {
        Result<Boolean> result = new Result<>();
        Campaign campaignToBeDeleted = findById(campaign.getCampaignId());
        if (campaign.getUserId() != campaignToBeDeleted.getUserId()) {
            result.addMessage("You must be the game master to delete this campaign", ResultType.INVALID);
        }else{
            result.setPayload(campaignRepository.deleteById(campaignToBeDeleted.getCampaignId()));
        }
        return result;
    }

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

        Campaign campaignToBeUpdated = findById(campaign.getCampaignId());
        if (campaign.getUserId() != campaignToBeUpdated.getUserId()) {
            result.addMessage("You must be the game master to edit this campaign", ResultType.INVALID);
            return result;
        }

        if (!campaignRepository.update(campaign)) {
            String message = String.format("campaignId: %s, not found", campaign.getCampaignId());
            result.addMessage(message, ResultType.NOT_FOUND);
            return result;
        }

        return result;
    }

    public Result<Void> createUser(CampaignUser cu) {
        Result<Void> result = validate(cu);
        if (!result.isSuccess()) {
            return result;
        }

        if (!campaignUserRepository.add(cu)) {
            result.addMessage("user not added", ResultType.INVALID);
        }

        return result;
    }

    public boolean deleteUserById(int campaignId, int userId) {
        return campaignUserRepository.delete(campaignId, userId);
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

    private Result<Void> validate(CampaignUser campaignUser) {
        Result<Void> result = new Result<>();
        if (campaignUser == null) {
            result.addMessage("Campaign cannot be null", ResultType.INVALID);
            return result;
        }

        if (campaignUser.getUser() == null) {
            result.addMessage("There must be a user that is a part of the campaign ", ResultType.INVALID);
        }

        return result;
    }
}
