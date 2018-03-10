/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.interfaces.IFriendshipActions;
import java.util.ArrayList;
import java.util.List;
import pojos.Friendship;
import utils.MySQLUtils;
import utils.QueriesUtils;

/**
 *
 * @author Gil
 */
public class FriendshipActions implements IFriendshipActions{

    @Override
    public boolean updateFriendListAfterAddingAUser(int userId, List<String> friends) {
        Friendship friendshipFromDB = (Friendship) MySQLUtils.retrieveById(Friendship.class, userId);
        if (friendshipFromDB != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Friendship> getUserFriendsList(String FbuserId) {
        String query = QueriesUtils.createQueryForListOfFacebookFriends(FbuserId);
        List relevantFriends = MySQLUtils.runQuery(query);
        List <Friendship> userFriends = new ArrayList<Friendship>();
        
        for (Object friend : relevantFriends) {
            Friendship friendship = (Friendship) friend;
            userFriends.add(friendship);
        }
        
        return userFriends;
    }
    
    @Override
    public boolean deleteUserById(int id) {        
        Friendship friendship = (Friendship) MySQLUtils.retrieveById(Friendship.class, id);
        if (friendship != null) {
            MySQLUtils.delete(friendship);
            return true;
        } else {
            return false;
        }
    }
}
