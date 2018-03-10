/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.interfaces.IFavoriteActions;
import dao.interfaces.IMatchesActions;
import dao.interfaces.IPostActions;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.Favorite;
import pojos.User;
import utils.MySQLUtils;
import utils.QueriesUtils;

/**
 *
 * @author or
 */
public class FavoritesActions implements IFavoriteActions {

    @Override
    public List<JSONObject> getAllFavorites(int id) {
        List allFavorites = MySQLUtils.runQuery(QueriesUtils.getFavoriteByUserId(id));
        return allFavorites.isEmpty() ? null : getFavoritesDetailsByIds(id, allFavorites);
    }

    @Override
    public boolean addNewFavorite(int userId, int matchingUserId) throws Exception{
        try {
            Favorite fav = new Favorite();
            fav.setUserId(userId);
            fav.setMatchingUserId(matchingUserId);
            
            EntityManager em = MySQLUtils.getEntityManager();
            MySQLUtils.beginTransaction(em);
            MySQLUtils.persist(fav, em);
            MySQLUtils.commitTransaction(em);
            
            return MatchesActions.checkIfThereIsFavorite(matchingUserId, userId);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public boolean deleteFavoriteById(int userId, int matchingUserId) {
        String query = QueriesUtils.getQueryForGetFavoriteOfUserAndMatchingUser(userId, matchingUserId);
        List favorites = MySQLUtils.runQuery(query);
        Favorite fav = null;
        if (favorites.size() == 1) {
            fav = (Favorite) favorites.get(0);
        }
        if (fav != null) {
            MySQLUtils.delete(fav);
            return true;
        } else {
            return false;
        }
    }

    private List<JSONObject> getFavoritesDetailsByIds(int userId, List allFavorites) {
        List<JSONObject> allFavoritesJsons = new ArrayList<JSONObject>();
        IMatchesActions matchActions = new MatchesActions();

        for (Object favoriteObj : allFavorites) {
            Favorite favorite = (Favorite) favoriteObj;
            int matchingUserId = favorite.getMatchingUserId();
            JSONObject matchDetails;

            if (checkIfUserIsOfferer(userId)) {
                matchDetails = matchActions.getOffererMatchDetailsByOffererAndSeeker(matchingUserId, userId);
            } else {
                matchDetails = matchActions.getSeekerMatchDetailsByOffererAndSeeker(userId, matchingUserId);
            }
            try {
                matchDetails.put("favoriteId", favorite.getFavoriteId());
                if (matchDetails != null) {
                    allFavoritesJsons.add(matchDetails);
                }
            } catch (JSONException ex) {
            }

        }

        return allFavoritesJsons;
    }

    private boolean checkIfUserIsOfferer(int userId) {
        User user = new UserActions().findUserById(userId);
        return user.getType().equalsIgnoreCase(configuration.Configuration.Types.offererType);
    }
}
