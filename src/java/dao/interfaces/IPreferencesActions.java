/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import configuration.Configuration;
import java.util.ArrayList;
import pojos.interfaces.IUserPreferences;
import java.util.List;
import org.json.JSONObject;
import pojos.UsersPreffSelection;
import utils.MySQLUtils;

/**
 *
 * @author or
 */
public abstract class IPreferencesActions {
    public abstract List<IUserPreferences> getAllUserPreferences();
    public abstract IUserPreferences findUserPreferencesById(int id);
    public abstract String createUserPreferences(IUserPreferences userPreferences);
    public abstract boolean deleteUserPreferencesById(int id);
    public abstract String updateUserPreferences(int id, IUserPreferences userPreferences);
    protected abstract boolean checkConcretePrefValidation(IUserPreferences userPref);
    public abstract IUserPreferences createConcretePreferencesFromJsonObject(JSONObject jason);
    
    public String checkValidation(IUserPreferences userPref) {
        String error = null;
        boolean isValid = false;
        
        if (userPref != null) {
            if (checkBasicPrefValidation(userPref)) {
                if (checkConcretePrefValidation(userPref)) {
                    isValid = true;
                } 
            }
            if (!isValid)
                error = "The parameters of the prefferences are invalid";
            
        } else {
            error = "The json object of the prefferences is incorrect.";
        }
        
        return error;
    }

    private Boolean checkBasicPrefValidation(IUserPreferences userPref) {
        int kosherFood = userPref.getKosher();
        int pets = userPref.getAnimals();
        int sharedExpenses = userPref.getSharedExpences();
        int smoking = userPref.getSmoking();
        int vegeterian = userPref.getVegan();
        
        Boolean valid = true;
        
        if (kosherFood < Configuration.Prefferences.MIN_VALUE_OF_PREF || kosherFood > Configuration.Prefferences.MAX_VALUE_OF_PREF ||
            pets < Configuration.Prefferences.MIN_VALUE_OF_PREF || pets > Configuration.Prefferences.MAX_VALUE_OF_PREF ||
            sharedExpenses < Configuration.Prefferences.MIN_VALUE_OF_PREF || sharedExpenses > Configuration.Prefferences.MAX_VALUE_OF_PREF ||
            smoking < Configuration.Prefferences.MIN_VALUE_OF_PREF || smoking > Configuration.Prefferences.MAX_VALUE_OF_PREF ||
            vegeterian < Configuration.Prefferences.MIN_VALUE_OF_PREF || vegeterian > Configuration.Prefferences.MAX_VALUE_OF_PREF) {
            valid = false;
        }
        
        return valid;
    }
    
    private void countDontCarePreff(UsersPreffSelection userSelection, IUserPreferences userPref, boolean updateOld) {
        int addValue = updateOld? -1 : 1;
        
        if (userPref.getAnimals() == 0) {
            userSelection.setAnimals(userSelection.getAnimals() + addValue);
        }
        
        if (userPref.getGayFriendly() == 0) {
            userSelection.setGayFriendly(userSelection.getGayFriendly() + addValue);

        }
        
        if (userPref.getKosher() == 0) {
            userSelection.setKosher(userSelection.getKosher() + addValue);
        }
        
        
        if (userPref.getMusicianFriendly()== 0) {
            userSelection.setMusicianFriendly(userSelection.getMusicianFriendly()+ addValue);
        }
        
        if (userPref.getNightLife()== 0) {
            userSelection.setNightLife(userSelection.getNightLife()+ addValue);
        }
        
        if (userPref.getSharedExpences()== 0) {
            userSelection.setSharedExpences(userSelection.getSharedExpences()+ addValue);
        }
        
        if (userPref.getVegan()== 0) {
            userSelection.setVegan(userSelection.getVegan()+ addValue);
        }
        
        if (userPref.getSmoking()== 0) {
            userSelection.setsmoking(userSelection.getSmoking()+ addValue);
        }
    }

    private void countMvpPreff(UsersPreffSelection userSelection, IUserPreferences userPref, boolean updateOld) {
        List<String> userMvpSelection = new ArrayList<>();
        
        int addValue = updateOld? -1 : 1;
        
        if (userPref.getFirstValuablePref() != null) {
            userMvpSelection.add(userPref.getFirstValuablePref());
        }
        
        if (userPref.getSecondValuablePref() != null) {
            userMvpSelection.add(userPref.getSecondValuablePref());
        }
        
        if (userPref.getThirdValuablePref() != null) {
            userMvpSelection.add(userPref.getThirdValuablePref());
        }
        
        if (userMvpSelection.contains("animal")) {
            userSelection.setAnimals(userSelection.getAnimals() + addValue);
        }
        
        if (userMvpSelection.contains("gayFriendly")) {
            userSelection.setGayFriendly(userSelection.getGayFriendly() + addValue);

        }
        
        if (userMvpSelection.contains("kosher")) {
            userSelection.setKosher(userSelection.getKosher() + addValue);
        }
        
        
        if (userMvpSelection.contains("musicianFriendly")) {
            userSelection.setMusicianFriendly(userSelection.getMusicianFriendly()+ addValue);
        }
        
        if (userMvpSelection.contains("nightLife")) {
            userSelection.setNightLife(userSelection.getNightLife()+ addValue);
        }
        
        if (userMvpSelection.contains("sharedExpences")) {
            userSelection.setSharedExpences(userSelection.getSharedExpences()+ addValue);
        }
        
        if (userMvpSelection.contains("vegan")) {
            userSelection.setVegan(userSelection.getVegan()+ addValue);
        }
        
        if (userMvpSelection.contains("smoking")) {
            userSelection.setsmoking(userSelection.getSmoking()+ addValue);
        }
    }
    
    public List<UsersPreffSelection> getUserPreffSelectionsFromDB() {
        List<UsersPreffSelection> usersSeletectionFromDB = (List<UsersPreffSelection>) (List<?>) MySQLUtils.retrieveAll(UsersPreffSelection.class);
        return usersSeletectionFromDB;
    }
    
    public List<UsersPreffSelection> createUserSelectionByUserPref(IUserPreferences userPref) {
        List<UsersPreffSelection> usersSeletectionFromDB = getUserPreffSelectionsFromDB();
        UsersPreffSelection dontCareSelection = null, mvpSelection= null;
        
        for (UsersPreffSelection preffSelection : usersSeletectionFromDB) {
            if (preffSelection.getType() == Configuration.PreffSelectionTableTypes.dontCare) {
                dontCareSelection = preffSelection;
            } else {
                mvpSelection = preffSelection;
            }
        }
        
        if (dontCareSelection == null || mvpSelection == null) {
            return null;
        }
        
        List<UsersPreffSelection> userSelections = new ArrayList<UsersPreffSelection>();
        
        countDontCarePreff(dontCareSelection, userPref, false);
        userSelections.add(dontCareSelection);
        
        countMvpPreff(mvpSelection, userPref, false);
        userSelections.add(mvpSelection);

        return userSelections;
    }
    
    public void updateUserSelectionAfterChange(IUserPreferences oldUserPreff, List<UsersPreffSelection> usersPreffSelection) {
        UsersPreffSelection dontCareSelection = null, mvpSelection= null;
        
        for (UsersPreffSelection preffSelection : usersPreffSelection) {
            if (preffSelection.getType() == Configuration.PreffSelectionTableTypes.dontCare) {
                dontCareSelection = preffSelection;
            } else {
                mvpSelection = preffSelection;
            }
        }
        
        countDontCarePreff(dontCareSelection, oldUserPreff, true);
        countMvpPreff(mvpSelection, oldUserPreff, true);
    }
}
