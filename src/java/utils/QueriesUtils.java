/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import pojos.ApartmentDetails;
import pojos.Conversation;
import pojos.Favorite;
import pojos.Friendship;
import pojos.Message;
import pojos.OffererPreferences;
import pojos.Post;
import pojos.RoomDetails;
import pojos.SeekerPreferences;
import pojos.User;
import pojos.UserSettings;

/**
 *
 * @author or
 */
public class QueriesUtils {

    public static String createQueryForSeekerMatches(SeekerPreferences seekerPref, User seekerUser) {
        String query = "select s, u, r, a, us, p\n"
                + "from " + User.class.getName() + " u, " + RoomDetails.class.getName() + " r, "
                + ApartmentDetails.class.getName() + " a, " + OffererPreferences.class.getName() + " s, "
                + UserSettings.class.getName() + " us, " + Post.class.getName() + " p\n"
                + "where u.id = s.userId and\n"
                + "	  a.offererUserId = s.userId and\n"
                + "	  a.apartmentId = r.apartmentId and\n"
                + "         p.offererId = u.id and\n"
                + "         us.userId = u.id and\n"
                + "	  r.price >= " + seekerPref.getMinPricePreffered() + " and\n"
                + "         r.price <= " + seekerPref.getMaxPricePreffered() + " and\n"
                + "	  a.totalRoomates <= " + seekerPref.getNumberOfRoomates();

        return query;
    }

    public static String createQueryForOffererMatch(OffererPreferences offererPref, User offererUser, RoomDetails room, ApartmentDetails apartment) {
        String query = "select s, u, us\n"
                + "from " + User.class.getName() + " u, " + SeekerPreferences.class.getName() + " s, "
                + UserSettings.class.getName() + " us\n"
                + "where u.id = s.userId and\n"
                + "         us.userId = u.id and\n"
                + "	  s.minPricePreffered <= " + room.getPrice() + " and\n"
                + "         s.maxPricePreffered >= " + room.getPrice() + " and\n"
                + "	  s.numberOfRoomates >= " + apartment.getTotalRoomates();

        return query;
    }

    public static String createQueryForOffererDetailsByUserId(int offererId) {
        String query = "select s, r, a, us, p\n"
                + "from " + User.class.getName() + " u, " + RoomDetails.class.getName() + " r, "
                + ApartmentDetails.class.getName() + " a, " + OffererPreferences.class.getName() + " s, "
                + UserSettings.class.getName() + " us, " + Post.class.getName() + " p\n"
                + "where s.userId = " + offererId + " and\n"
                + "	  a.offererUserId = s.userId and\n"
                + "	  a.apartmentId = r.apartmentId and\n"
                + "         p.offererId = s.userId and\n"
                + "         us.userId = s.userId";
        return query;
    }

    public static String createQueryForOffererDetailsByOffererId(int offererId) {
        String query = "select u, s, r, a, p\n"
                + "from " + User.class.getName() + " u, " + RoomDetails.class.getName() + " r, "
                + ApartmentDetails.class.getName() + " a, " + OffererPreferences.class.getName() + " s, "
                + Post.class.getName() + " p\n"
                + "where p.offererId = " + offererId + " and\n"
                + "	  a.offererUserId = s.userId and\n"
                + "	  a.apartmentId = r.apartmentId and\n"
                + "       p.offererId = s.userId and \n"
                + "       p.roomId = r.roomId and\n"
                + "       u.id = s.userId";
        return query;
    }

    public static String createQueryForSeekerDetailsByid(int seekerId) {
        String query = "select u, s\n"
                + "from " + User.class.getName() + " u, " + SeekerPreferences.class.getName() + " s\n"
                + "where u.id = " + seekerId + " and\n"
                + "u.id = s.userId";
        return query;
    }

    public static String createQueryForConversationIdByUsers(int user_one, int user_two) {
        String query = "select c.conversationId \n"
                        + "from " + Conversation.class.getName() + " c where"
                        + "(c.user_one = " + user_one + " and c.user_two = " + user_two + ")"
                        + " or (c.user_one = " + user_two + " and c.user_two = " + user_one + ")";
        return query;
    }

    public static String createQueryToPullUsersHistory(int convId, int lastMessageId) {
                String query = "select m \n"
                        + "from " + Message.class.getName() + " m where"
                        + " m.convId = " + convId + " and m.messageId > " + lastMessageId;
        return query;
    }
    
    public static String createQueryToPullUsersHistoryByConversationId(int convId) {
        String query = "select m \n"
                + "from " + Message.class.getName() + " m where"
                + " m.convId = " + convId;
        return query;
    }
    
    public static String createQueryForConversationByUser(int userId) {
        String query = "select c \n"
                        + "from " + Conversation.class.getName() + " c where "
                        + "c.user_one = " + userId + " or c.user_two = " + userId;
        return query;
    }
    
    public static String createQueryForListOfFacebookFriends(String userId) {
        String query = "select distinct f \n"
                        + "from " + Friendship.class.getName() + " f where "
                        + "f.userOnefacebookID = " + userId + " or f.userTwoFacebookID =  " + userId;
        return query;
    }

    public static String getQueryForGetFavoriteOfUserAndMatchingUser(int userId, int matchingUserId) {                
        return "select f from " + Favorite.class.getName() + " f\n"
                + " where f.userId = " + userId + " and f.matchingUserId = " + matchingUserId;
    }

    public static String getFavoriteByUserId(int id) {
        return "select c from " + Favorite.class.getName() + " c where c.userId = " + id;
    }
    
    public static String getUsersWhoWantToReciveNotfiactions() {
        return "select u, us "
                + "from " + User.class.getName() + " u, " + UserSettings.class.getName() + " us "
                + "where u.id = us.userId and us.notifications = 1 and us.email != null and us.email != \"\"";
    }

    public static String createQueryForDeleteSeekerTestUser(int id, String facebookId) {
        StringBuilder sb = new StringBuilder();
        sb.append(/*"delete from " + Favorite.class.getName() + " f\n" +
"where f.userId = " + id + 
" ;\n" +
"delete from " + Message.class.getName() + "\n" +
"where convId in (select convId\n" +
"		  from " + Conversation.class.getName() + "\n" +
"                 where user_one = " + id + " or user_two = " + id + ")\n" +
"\n" +
"delete from " + Conversation.class.getName() + "\n" +
"where user_one = " + id + " or user_two = " + id + "\n" +
"\n" +
"delete from " + Friendship.class.getName() + " fr\n" +
"where fr.userOnefacebookID = '" + facebookId + "' or fr.userTwofacebookID = '" + facebookId + "' ;\n" +
"\n" +*/
"delete from " + UserSettings.class.getName() + " us\n" +
"where us.userId = " + id + " ;\n" +
"\n" +
"delete from " + SeekerPreferences.class.getName() + " s\n" +
"where s.userId = " + id + " ;\n" +
"\n" +
"delete from " + User.class.getName() + " u\n" +
"where u.id = " + id + " ;");
        return sb.toString();
    }
    
    public static String createQueryForDeleteUserSettings(int id) {
        return "delete from " + UserSettings.class.getName() + " us\n" +
            "where us.userId = " + id;
    }
    
    public static String createQueryForDeleteSeekerPref(int id) {
        return "delete from " + SeekerPreferences.class.getName() + " s\n" +
                "where s.userId = " + id;
    }
    
    public static String createQueryForDeleteUser(int id) {
        return "delete from " + User.class.getName() + " u\n" +
                "where u.id = " + id;
    }
    
    public static String createQueryForDeleteFriendship(String facebookId) {
        return "delete from " + Friendship.class.getName() + " fr\n" +
        "where fr.userOnefacebookID = '" + facebookId + "' or fr.userTwoFacebookID = '" + facebookId + "'";
    }

    public static String createQueryForDeleteOffererPref(int id) {
        return "delete from " + OffererPreferences.class.getName() + " s\n" +
                "where s.userId = " + id;
    }
    
    public static String createQueryForDeleteRoomDetails(int id) {
        return "delete from " + RoomDetails.class.getName() + " r\n" +
                "where r.roomId = " + id;
    }
    
    public static String createQueryForDeleteApartmentDetails(int id) {
        return "delete from " + ApartmentDetails.class.getName() + " r\n" +
                "where r.apartmentId = " + id;
    }
    
    public static String createQueryForDeletePost(int offererId) {
        return "delete from " + Post.class.getName() + " r\n" +
                "where r.offererId = " + offererId;
    }

    public static String getPostByOffererId(int id) {
        return "select c from " + Post.class.getName()+ " c where c.offererId = " + id;
    }
}
