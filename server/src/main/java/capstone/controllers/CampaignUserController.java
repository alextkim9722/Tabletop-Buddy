package capstone.controllers;

import capstone.models.CampaignUser;
import capstone.service.CampaignService;
import capstone.service.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaign/user")
public class CampaignUserController {

    private final CampaignService service;

    public CampaignUserController(CampaignService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody CampaignUser campaignUser) {
        Result<Void> result = service.createUser(campaignUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{campaignId}/{userId}")
    public ResponseEntity<Void> deleteByKey(@PathVariable int campaignId, @PathVariable int userId) {
        if (service.deleteUserById(campaignId, userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
