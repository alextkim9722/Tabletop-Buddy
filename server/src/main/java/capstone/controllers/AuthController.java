package capstone.controllers;

import capstone.models.Campaign;
import capstone.models.User;
import capstone.security.JwtConverter;
import capstone.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtConverter jwtConverter;
    private final UserService service;

    public AuthController(AuthenticationManager authManager, JwtConverter jwtConverter, UserService service) {
        this.authManager = authManager;
        this.jwtConverter = jwtConverter;
        this.service = service;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authManager.authenticate(token);
        if (authentication.isAuthenticated()) {
            String jwtToken = jwtConverter.getTokenFromUser((User) authentication.getPrincipal());
            HashMap<String, String> whateverMap = new HashMap<>();
            whateverMap.put("jwt_token", jwtToken);
            return new ResponseEntity<>(whateverMap, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PostMapping("/create_account")
    public ResponseEntity<Map<String, Integer>> createAccount(@RequestBody Map<String, String> credentials) {
        User user;

        String username = credentials.get("username");
        String password = credentials.get("password");
        String city = credentials.get("city");
        String state = credentials.get("state");
        String description = credentials.get("description");

        user = service.create(username, password, city, state, description);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("appUserId", user.getUserId());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}
