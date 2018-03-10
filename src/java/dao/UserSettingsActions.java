package dao;

import dao.interfaces.IUserSettingActions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import utils.MySQLUtils;
import pojos.UserSettings;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import utils.MailUtils;


public class UserSettingsActions implements IUserSettingActions{
    private static final int MIN_PERCENT_TO_SHOW_MATCHES = 60;
    private static final int MIN_PERCENT_TO_INCLUDE_ME_IN_MATCHES = 50;

    public static UserSettings createDefaultSettings() {
        UserSettings userSettings = new UserSettings();
        
        userSettings.setMinIncludeMatch(MIN_PERCENT_TO_INCLUDE_ME_IN_MATCHES);
        userSettings.setMinShowMatch(MIN_PERCENT_TO_SHOW_MATCHES);
        userSettings.setNotification(false);
        userSettings.setEmail(null);
        
        return userSettings;
    }
    
    @Override
    public List<UserSettings> getAllUsersSettings() {
        List<UserSettings> settings = (List<UserSettings>)(List<?>) MySQLUtils.retrieveAll(UserSettings.class);
        if (settings.isEmpty()) {
            return null;
        } else {
            return settings;
        } 
    }

    @Override
    public UserSettings findUserSettingsById(int id) {
        UserSettings settings = (UserSettings) MySQLUtils.retrieveById(UserSettings.class ,id);
        if (settings != null) {
            return settings;
        } else {
            return null;
        } 
    }

    @Override
    public void createUserSettings(UserSettings settings) {  
        try {
            setDefaultValues(settings);
            EntityManager em = MySQLUtils.getEntityManager();
            MySQLUtils.beginTransaction(em);
            MySQLUtils.persist(settings, em);
            MySQLUtils.commitTransaction(em);
        } catch (Exception ex) {
            Logger.getLogger(UserSettingsActions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean deleteUserSettingsById(int id) {
        UserSettings settings = (UserSettings) MySQLUtils.retrieveById(UserSettings.class, id);
        if (settings != null) {
            MySQLUtils.delete(settings);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> updateUserSettings(int id, UserSettings settings) {
        List<String> errors = checkValidation(settings);
        if (errors != null && !errors.isEmpty()){
            return errors;
        }
        
        settings.setNotification(settings.getEmail() != null && !settings.getEmail().isEmpty());
        UserSettings settingsFromDB = (UserSettings) MySQLUtils.retrieveById(UserSettings.class, id);
        if (settingsFromDB != null) {
            settings.setUserId(settingsFromDB.getUserId());
            MySQLUtils.Update(settings);
            return null;
        } else {
            errors = new ArrayList<String>();
            errors.add("There is no userStettings with userId " + id);
            return errors;
        }
    }

    private void setDefaultValues(UserSettings settings) {
        settings.setMinIncludeMatch(MIN_PERCENT_TO_INCLUDE_ME_IN_MATCHES);
        settings.setMinShowMatch(MIN_PERCENT_TO_SHOW_MATCHES);
    }

    public boolean checkIfMailNeedToBeSendToUser(UserSettings recipientUserSettings) {
        final long NUM_OF_TICKS_PER_DAY = 86_400_000;
        
        String recipientUserEmail = recipientUserSettings.getEmail();
        boolean isUserWantNotification = recipientUserSettings.isNotifications() && recipientUserEmail != null && !recipientUserEmail.isEmpty();
        boolean lastMailSentMoreThanADay = false;
        
        if (isUserWantNotification) {
            long lastTimeMailSent = recipientUserSettings.getLastTimeMailSent();
        
            long nowDateTime = Calendar.getInstance().getTime().getTime();
            lastMailSentMoreThanADay = lastTimeMailSent + NUM_OF_TICKS_PER_DAY <= nowDateTime;
        }
        
        return isUserWantNotification && lastMailSentMoreThanADay;
    }

    private List<String> checkValidation(UserSettings settings) {
        List<String> errors = MailUtils.checkEmailValidation(settings.getEmail());
        
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        
        if (settings.getMinIncludeMatch() < 0 || settings.getMinIncludeMatch() > 100) {
            errors.add("minIncludeMatch is invalid");
        }
        
        if (settings.getMinShowMatch()< 0 || settings.getMinShowMatch() > 100) {
            errors.add("minShowMatch is invalid");
        }
        
        return errors;
    }
}
