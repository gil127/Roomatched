/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import java.util.List;
import org.json.JSONObject;
import pojos.Favorite;

/**
 *
 * @author or
 */
public interface IFavoriteActions {
    List<JSONObject> getAllFavorites(int id);
    boolean addNewFavorite(int userId, int matchingUserId) throws Exception;
    boolean deleteFavoriteById(int userId, int matchingUserId);
}
