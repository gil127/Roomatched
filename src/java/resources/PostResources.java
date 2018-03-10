/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import utils.ResourceUtils;
import pojos.Post;
import dao.interfaces.IPostActions;
import dao.PostsActions;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

/**
 *
 * @author Gil
 */
@Path("/post")
public class PostResources {
 private final IPostActions postActions;
    
    @Context 
    HttpServletResponse response;

    public PostResources() {
        postActions = new PostsActions();
    }
    
    @GET
    @Produces({"application/json;charset=utf-8"})
    public void getAllPosts() {
        List<JSONObject> postsList = postActions.getAllPosts();
        ResourceUtils.okResponse(response, postsList.toString());
    }
    
    @POST
    @Consumes("application/json;charset=utf-8")
    public void addNewPost(Post post) {
        postActions.addNewPost(post.getPostId(), post.getRoomId(), post.getOffererId());
        ResourceUtils.okResponse(response);
    }
    
    @POST
    @Path("/delete/{id}")
    public void deleteMatchById(@PathParam("id") int id) {
        boolean succeedToDelete = postActions.deletePostById(id);
        if (!succeedToDelete) {
            ResourceUtils.errorResponse(response, "Could not delete post with id " + id);
        } else {
            ResourceUtils.okResponse(response);
        }
    }
    
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public void getPostByOffererId(@PathParam("id") int id) {
        JSONObject post = postActions.getJsonOfPostByOffererId(id);
        ResourceUtils.okResponse(response, post.toString());
    }
}
