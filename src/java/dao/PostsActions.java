/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.interfaces.IPostActions;
import utils.MySQLUtils;
import pojos.ApartmentDetails;
import pojos.Post;
import pojos.RoomDetails;
import pojos.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JSONObjectUtils;
import utils.QueriesUtils;

/**
 *
 * @author Gil
 */
public class PostsActions implements IPostActions{

    @Override
    public List<JSONObject> getAllPosts() {
        List<Post> posts = (List<Post>)(List<?>) MySQLUtils.retrieveAll(Post.class);
        List<User> users = getAllUsersFromPosts(posts);
        List<RoomDetails> rooms = getAllRoomsFromPosts(posts);
        List<ApartmentDetails> apartments = getAllApartments(rooms);
        
        List<JSONObject> results = new ArrayList<>();
        
        if (!posts.isEmpty() && (posts.size() == rooms.size()) && (users.size() == apartments.size())
                && users.size() == posts.size()) {
            
            for (int i = 0; i < posts.size(); i++) {
                int postId = posts.get(i).getPostId();
                results.add(JSONObjectUtils.getPostJsonObjectFromPojos(postId, users.get(i), rooms.get(i), apartments.get(i)));
            }
            
            return results;
        } else {
            return null;
        }  
    }

    @Override
    public void addNewPost(int postId, int roomId, int offererId) {
        try {
            Post post = new Post();
            
            post.setOffererId(offererId);
            post.setPostId(postId);
            post.setRoomId(roomId);
            
            EntityManager em = MySQLUtils.getEntityManager();
            MySQLUtils.beginTransaction(em);
            
            MySQLUtils.persist(post, em);
            MySQLUtils.commitTransaction(em);
        } catch (Exception ex) {
            Logger.getLogger(PostsActions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean deletePostById(int id) {
        Post postToDelete = (Post) MySQLUtils.retrieveById(Post.class, id);
        if (postToDelete != null) {
            MySQLUtils.delete(postToDelete);
            return true;
        } else {
            return false;
        }
    }

    private List<User> getAllUsersFromPosts(List<Post> posts) {
        List<User> users = new ArrayList<User>();
        
        for(Post post : posts) {
            users.add(getUserByUserId(post.getOffererId()));
        }
        
        return users;
    }

    private List<RoomDetails> getAllRoomsFromPosts(List<Post> posts) {
        List<RoomDetails> rooms = new ArrayList<RoomDetails>();
        
        for(Post post : posts) {
            rooms.add(getRoomByRoomId(post.getRoomId()));
        }
        
        return rooms;
    }

    private List<ApartmentDetails> getAllApartments(List<RoomDetails> rooms) {
        List<ApartmentDetails> apartments = new ArrayList<ApartmentDetails>();
        
        for(RoomDetails room : rooms) {
            apartments.add(getApartmentFromApartmentId(room.getApartmentId()));
        }
        
        return apartments;
    }

    @Override
    public JSONObject getJsonOfPostByOffererId(int id) {
        Post post = getPostFromOffererId(id);
        User user = getUserByUserId(id);
        RoomDetails room = getRoomByRoomId(post.getRoomId());
        ApartmentDetails apartment = getApartmentFromApartmentId(room.getApartmentId());
        return JSONObjectUtils.getPostJsonObjectFromPojos(post.getPostId(), user, room, apartment);
    }

    private RoomDetails getRoomByRoomId(int roomId) {
        return (RoomDetails) MySQLUtils.retrieveById(RoomDetails.class, roomId);
    }

    private ApartmentDetails getApartmentFromApartmentId(int apartmentId) {
        return (ApartmentDetails) MySQLUtils.retrieveById(ApartmentDetails.class, apartmentId);
    }

    private User getUserByUserId(int offererId) {
        return (User) MySQLUtils.retrieveById(User.class, offererId);
    }

    private Post getPostFromOffererId(int id) {
        List resFromQuery = MySQLUtils.runQuery(QueriesUtils.getPostByOffererId(id));
        return (Post)resFromQuery.get(0);
    }
}
