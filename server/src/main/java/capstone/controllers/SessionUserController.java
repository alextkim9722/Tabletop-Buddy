package capstone.controllers;

import capstone.models.SessionUser;
import capstone.service.Result;
import capstone.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session/user")
public class SessionUserController {

    private final SessionService sessionService;

    public SessionUserController(SessionService sessionService) { this.sessionService = sessionService; }

    @PostMapping
    public ResponseEntity<Object> addSessionUser(@RequestBody SessionUser su) {
        Result<Void> result = sessionService.createUser(su);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{sessionId}/{userId}")
    public ResponseEntity<Void> deleteByKey(@PathVariable int sessionId, @PathVariable int userId) {
        if (sessionService.deleteUserByKey(sessionId, userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
