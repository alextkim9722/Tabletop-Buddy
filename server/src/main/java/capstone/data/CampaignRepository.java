package capstone.data;

import capstone.models.Campaign;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface CampaignRepository {
    List<Campaign> findAll();

    @Transactional
    Campaign findById(int campaignId);

    @Transactional
    List<Campaign> findbyTag(String type, int players, int size, Timestamp start);

    Campaign add(Campaign campaign);

    boolean update (Campaign campaign);

    @Transactional
    boolean deleteById(int campaignId);
}

