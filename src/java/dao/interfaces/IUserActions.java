package dao.interfaces;

import org.json.JSONObject;
import pojos.User;
import java.util.List;

public interface IUserActions {
    List<User> getAllUsers();
    User findUserById(int id);
    User findUserByFacebookId(String fbId);
    List<String> createUser(JSONObject jason) throws Exception;
    boolean deleteUserById(int id);
    List<String> updateUser(int id, User user);
}