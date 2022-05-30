package capstone.service;

import capstone.data.UserRepository;
import capstone.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import javax.validation.ValidationException;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);

        if (user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return user;
    }

    public User create(String username, String password, String city, String state, String description) {
        validateName(username);
        validatePassword(password);
        validateCity(city);
        validateState(state);

        password = encoder.encode(password);

        User user = new User(0, username, password, false, List.of("User"));
        user.setState(state);
        user.setCity(city);
        user.setDescription(description);

        return repository.create(user);
    }

    public boolean deleteById(int userId) { return repository.deleteById(userId); }

    private void validateName(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("username is required");
        }

        if (username.length() > 250) {
            throw new ValidationException("username must be less than 250 characters");
        }
    }

    private void validateCity(String city) {
        if (city == null || city.isBlank()) {
            throw new ValidationException("city is required");
        }

        if (city.length() > 250) {
            throw new ValidationException("city must be less than 250 characters");
        }
    }

    private void validateState(String state) {
        if (state == null || state.isBlank()) {
            throw new ValidationException("state is required");
        }

        if (state.length() > 250) {
            throw new ValidationException("state must be less than 250 characters");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("password must be at least 8 characters");
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            throw new ValidationException("password must contain a digit, a letter, and a non-digit/non-letter");
        }
    }
}
