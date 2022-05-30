package capstone.controllers;

import capstone.models.Campaign;
import capstone.models.Filter;
import capstone.service.CampaignService;
import capstone.service.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController (CampaignService campaignService) { this.campaignService = campaignService; }

    @GetMapping
    public List<Campaign> findAll() { return campaignService.findAll(); }

    @GetMapping("/{campaignId}")
    public ResponseEntity<Object> findById(@PathVariable int campaignId) {
        Campaign campaign = campaignService.findById(campaignId);
        if (campaign == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(campaign, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<Object> findByFilter(@RequestBody Filter filter) {
        List<Campaign> campaignList = campaignService.findByTag(filter);
        if (campaignList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(campaignList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Campaign campaign) {
        Result<Campaign> result = campaignService.add(campaign);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{campaignId}")
    public ResponseEntity<Object> update(@PathVariable int campaignId, @RequestBody Campaign campaign) {
        if (campaignId != campaign.getCampaignId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Campaign> result = campaignService.update(campaign);
        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<Object> deleteById(@PathVariable int campaignId, @RequestBody Campaign campaign) {
        if (campaignId != campaign.getCampaignId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result <Campaign> result = campaignService.deleteById(campaign);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }
}
