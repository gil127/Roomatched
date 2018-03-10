package dao.interfaces;

import pojos.UserSettings;
import java.util.List;

public interface IUserSettingActions {
    List<UserSettings> getAllUsersSettings();
    UserSettings findUserSettingsById(int id);
    void createUserSettings(UserSettings permission);
    boolean deleteUserSettingsById(int id);
    List<String> updateUserSettings(int id, UserSettings userSettings);
}
