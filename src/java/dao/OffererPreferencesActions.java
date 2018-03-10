/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.interfaces.IPreferencesActions;
import utils.MySQLUtils;
import configuration.Configuration;
import pojos.interfaces.IUserPreferences;
import pojos.OffererPreferences;
import java.util.List;
import javax.persistence.EntityManager;
import org.json.JSONObject;
import pojos.UsersPreffSelection;
import utils.JSONObjectUtils;

/**
 *
 * @author or
 */
public class OffererPreferencesActions extends IPreferencesActions {

    @Override
    public List<IUserPreferences> getAllUserPreferences() {
        List<IUserPreferences> usersPreferences = (List<IUserPreferences>) (List<?>) MySQLUtils.retrieveAll(OffererPreferences.class);
        if (usersPreferences.isEmpty()) {
            return null;
        } else {
            return usersPreferences;
        }
    }

    @Override
    public IUserPreferences findUserPreferencesById(int id) {
        OffererPreferences userPreferences = (OffererPreferences) MySQLUtils.retrieveById(OffererPreferences.class, id);
        if (userPreferences != null) {
            return userPreferences;
        } else {
            return null;
        }
    }

    @Override
    public String createUserPreferences(IUserPreferences userPreferences) {
        String error = checkValidation(userPreferences);

        if (error == null) {
            try {
                EntityManager em = MySQLUtils.getEntityManager();
                MySQLUtils.beginTransaction(em);
                MySQLUtils.persist(userPreferences, em);
                MySQLUtils.commitTransaction(em);
            } catch (Exception ex) {
                return ex.getMessage();
            }
        }

        return error;
    }

    @Override
    public boolean deleteUserPreferencesById(int id) {
        IUserPreferences userPreferences = (IUserPreferences) MySQLUtils.retrieveById(OffererPreferences.class, id);
        if (userPreferences != null) {
            MySQLUtils.delete(userPreferences);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String updateUserPreferences(int id, IUserPreferences userPreferences) {
        String error;
        OffererPreferences userPreferencesFromDB = (OffererPreferences) MySQLUtils.retrieveById(OffererPreferences.class, id);

        if (userPreferencesFromDB != null) {
            userPreferences.setUserId(userPreferencesFromDB.getUserId());
            
            error = checkValidation(userPreferences);
            if (error == null) {
                List<UsersPreffSelection> usersPreffFromDB = this.createUserSelectionByUserPref(userPreferences);
                
                if (userPreferencesFromDB == null) {
                    return "userPreffSelection table is incorrect";
                }
                
                updateUserSelectionAfterChange(userPreferencesFromDB, usersPreffFromDB);
                
                try {
                    EntityManager em = MySQLUtils.getEntityManager();
                    MySQLUtils.beginTransaction(em);
                    MySQLUtils.Update(userPreferences, em);
                    
                    for (UsersPreffSelection userPreffSelection : usersPreffFromDB) {
                        MySQLUtils.Update(userPreffSelection, em);
                    }
                    
                    MySQLUtils.commitTransaction(em);
                } catch (Exception ex) {
                    return ex.getMessage();
                }
            }
        } else {
            error = "There are no preferences with the userID " + id;
        }
        
        return error;
    }

    @Override
    protected boolean checkConcretePrefValidation(IUserPreferences userPref) {
        OffererPreferences offererPref = (OffererPreferences) userPref;
        return offererPref.getSexPreffered().equalsIgnoreCase(Configuration.Gender.MALE)
                || offererPref.getSexPreffered().equalsIgnoreCase(Configuration.Gender.FEMALE)
                || offererPref.getSexPreffered().equalsIgnoreCase(Configuration.Gender.ANY);
    }

    @Override
    public IUserPreferences createConcretePreferencesFromJsonObject(JSONObject jason) {
        return JSONObjectUtils.createOffererPreferencesFromJsonObject(jason);
    }
}
