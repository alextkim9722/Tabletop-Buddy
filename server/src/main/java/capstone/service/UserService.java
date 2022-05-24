package capstone.service;

import capstone.data.UserRepository;
import capstone.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository){ this.repository = repository; }

    Result<User> create(User user) {}

    Result<User> update(User user) {}
    boolean delete(int id) {}
    Result<User> validate(User user) {}
}
