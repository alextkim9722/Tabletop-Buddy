package capstone.data;

import capstone.models.Session;
import capstone.models.User;

public interface UserRepository {
    User create(User user);
    User findByUsername(String username);
    void update(User user);
    boolean deleteById(int userId);
}
