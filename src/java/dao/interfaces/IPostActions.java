/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import pojos.Post;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Gil
 */
public interface IPostActions {
    List<JSONObject> getAllPosts();
    void addNewPost(int postId, int roomId, int offererId);
    boolean deletePostById(int id);
    JSONObject getJsonOfPostByOffererId(int id);
}
