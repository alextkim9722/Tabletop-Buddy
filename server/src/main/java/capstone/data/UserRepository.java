package capstone.data;


import capstone.models.User;




public interface UserRepository {
    User create(User user);
    User findByUsername(String username);
    User findById(int id);
    void update(User user);
    boolean deleteById(int userId);
}
