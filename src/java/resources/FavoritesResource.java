/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import dao.FavoritesActions;
import dao.interfaces.IFavoriteActions;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.LogError;
import utils.ErrorLogUtils;
import utils.MySQLUtils;
import utils.ResourceUtils;

/**
 *
 * @author or
 */
@Path("/favorite")
public class FavoritesResource {
    private final IFavoriteActions favoriteActions;
    
    @Context 
    HttpServletResponse response;

    public FavoritesResource() {
        favoriteActions = new FavoritesActions();
    }
    
    @POST
    @Consumes("application/json;charset=utf-8")
    public void addNewFavorite(String jsonAsString) throws Exception {
        try {
            JSONObject json = new JSONObject(jsonAsString);
            int userId = json.getInt("userId");
            int matchingUserId = json.getInt("matchingUserId");
            boolean isPerfectFavourite = favoriteActions.addNewFavorite(userId, matchingUserId);
            JSONObject json2 = new JSONObject();
            json2.put("perfectFavourite", isPerfectFavourite);
            ResourceUtils.okResponse(response, json2.toString());
        } catch (Exception ex) {
            ErrorLogUtils.saveErrorLog(jsonAsString, ex.getMessage(), "Add new favorite (error list is not empty)");
            ResourceUtils.errorResponse(response, "There are missing fields in the JSON object");
        }
    }
    
    @POST
    @Path("/delete")
    public void deleteFavoriteById(String jsonAsString) throws Exception {
        try {
            JSONObject json = new JSONObject(jsonAsString);
            int userId = json.getInt("userId");
            int matchingUserId = json.getInt("matchingUserId");
            boolean succeedToDelete = favoriteActions.deleteFavoriteById(userId, matchingUserId);
            if (!succeedToDelete) {
                ErrorLogUtils.saveErrorLog(jsonAsString, "could not delete favorite", "delete favorite (error list is not empty)");
                ResourceUtils.errorResponse(response, "Could not delete favorite with userId " + userId + " and matchingUserId = " + matchingUserId);
            } else {
                ResourceUtils.okResponse(response);
            }
        } catch (JSONException ex) {
            ErrorLogUtils.saveErrorLog(jsonAsString, ex.getMessage(), "delete favorite (exception)");
            ResourceUtils.errorResponse(response, "There are missing fields in the JSON object");
        }
    }
    
    @GET
    @Produces("application/json;charset=utf-8")
    @Path("/{id}")
    public void getAllFavoritesByUserId(@PathParam("id") int id) {
        List<JSONObject> allFavorites = favoriteActions.getAllFavorites(id);
        if (allFavorites != null && !allFavorites.isEmpty()) {
            ResourceUtils.okResponse(response, allFavorites.toString());
        } else {
            ResourceUtils.errorResponse(response, "There are no favorites for the user with the id: " + id);
        }
    }
}
