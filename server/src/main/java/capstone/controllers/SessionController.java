package capstone.controllers;

import capstone.models.Session;
import capstone.models.SessionUser;
import capstone.service.Result;
import capstone.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController (SessionService sessionService) { this.sessionService = sessionService; }

    @GetMapping("/{userId}")
    public List<Session> getFromUserId(@PathVariable int userId) {
        return sessionService.getFromUserId(userId);
    }

    @GetMapping("/{campaignId}")
    public List<Session> getFromCampaignId(@PathVariable int campaignId) {
        return sessionService.getFromCampaignId(campaignId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Session session) {
        Result<Session> result = sessionService.create(session);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<Object> update(@PathVariable int sessionId, @RequestBody Session session) {
        if (sessionId != session.getSessionid()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Session> result = sessionService.update(session);
        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteById(@PathVariable int sessionId) {
        if (sessionService.delete(sessionId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
