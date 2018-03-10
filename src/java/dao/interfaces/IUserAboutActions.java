/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import org.json.JSONObject;

/**
 *
 * @author Gil
 */
public interface IUserAboutActions {
    JSONObject getUserAbout(int id);
    boolean updateUserAbout(int id, String aboutToUpdate);
}
