package capstone.controllers;

import capstone.models.UserSchedule;
import capstone.service.Result;
import capstone.service.UserScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/userSchedule")
public class UserScheduleController {

    private final UserScheduleService service;

    public UserScheduleController(UserScheduleService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<UserSchedule> getFromUserId(@PathVariable int userId) {
        return service.getFromUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody UserSchedule userSchedule) {
        Result<UserSchedule> result = service.create(userSchedule);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{userScheduleId}")
    public ResponseEntity<Object> update(@PathVariable int userScheduleId, @RequestBody UserSchedule userSchedule) {
        if (userScheduleId != userSchedule.getUserScheduleId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<UserSchedule> result = service.update(userSchedule);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{userScheduleId}")
    public ResponseEntity<Void> deleteById(@PathVariable int userScheduleId) {
        if (service.delete(userScheduleId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/su/{sessionId}/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable int sessionId, @PathVariable int userId) {
        if (service.delete(sessionId, userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
