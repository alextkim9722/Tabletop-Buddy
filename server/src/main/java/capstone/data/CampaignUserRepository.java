package capstone.data;

import capstone.models.CampaignUser;

public interface CampaignUserRepository {
    boolean add(CampaignUser cu);
    boolean update(CampaignUser cu);
    boolean delete(int campaignId, int userId);
}
