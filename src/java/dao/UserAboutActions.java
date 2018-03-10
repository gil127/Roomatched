/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.interfaces.IUserAboutActions;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.User;
import utils.MySQLUtils;

/**
 *
 * @author Gil
 */
public class UserAboutActions  implements IUserAboutActions{
    
    @Override
    public JSONObject getUserAbout(int id) {
        User loggedInUser = (User) MySQLUtils.retrieveById(User.class ,id);
        
        if (loggedInUser != null) {
            try {
                JSONObject json = new JSONObject();
                json.put("about", loggedInUser.getAbout());
                return json;
            } catch (JSONException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean updateUserAbout(int id, String aboutToUpdate) {
        User userFromDB = (User) MySQLUtils.retrieveById(User.class, id);
        if (userFromDB != null) {
            userFromDB.setAbout(aboutToUpdate);
            MySQLUtils.Update(userFromDB);
            return true;
        } else {
            return false;
        }
    }
}
