/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import java.util.List;
import pojos.Friendship;

/** 
 *
 * @author Gil
 */
public interface IFriendshipActions {
    boolean updateFriendListAfterAddingAUser(int userId, List<String> friends);
    List<Friendship> getUserFriendsList(String userId);
    boolean deleteUserById(int id); 
}
