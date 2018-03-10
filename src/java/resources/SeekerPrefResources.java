/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import utils.ResourceUtils;
import pojos.interfaces.IUserPreferences;
import pojos.SeekerPreferences;
import dao.interfaces.IPreferencesActions;
import dao.SeekerPreferencesActions;
import java.util.ArrayList;
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
import org.json.JSONException;
import org.json.JSONObject;
import utils.ErrorLogUtils;
import utils.JSONObjectUtils;

/**
 *
 * @author or
 */
@Path("/seekerPref")
public class SeekerPrefResources {
    private final IPreferencesActions userPrefActions;
    
    @Context 
    HttpServletResponse response;

    public SeekerPrefResources() {
        userPrefActions = new SeekerPreferencesActions();
    }

    @GET
    @Produces("application/json;charset=utf-8")
    public void getAllSeekersPreferences() {
        List<IUserPreferences> allUserPreferences = userPrefActions.getAllUserPreferences();
        List<JSONObject> res = new ArrayList<JSONObject>();
        
        for (IUserPreferences userPref : allUserPreferences) {
            res.add(JSONObjectUtils.createJsonObjectFromSeekerPreferences((SeekerPreferences)userPref));
        }
        
        ResourceUtils.okResponse(response, res.toString());
    }

    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public void getSeekerPrefById(@PathParam("id") int id) {
        SeekerPreferences userPref = (SeekerPreferences)userPrefActions.findUserPreferencesById(id);
        JSONObject json = JSONObjectUtils.createJsonObjectFromSeekerPreferences(userPref);
        if (userPref == null) {
            ResourceUtils.errorResponse(response, "Could not find preferences with user id " + id);
        } else {
            ResourceUtils.okResponse(response, json.toString());
        }
    }

    @POST
    @Consumes("application/json;charset=utf-8")
    public void addNewSeekerPref(String seekerAsString) throws JSONException {
        JSONObject json = new JSONObject(seekerAsString);
        IUserPreferences userPref = JSONObjectUtils.createSeekerPreferencesFromJsonObject(json);
        String error = userPrefActions.createUserPreferences(userPref);
        if (error == null) {
            ResourceUtils.okResponse(response);
        } else {
            ResourceUtils.errorResponse(response, error);
        }
    }

    @POST
    @Path("/{id}")
    @Consumes("application/json;charset=utf-8")
    // The User object that arrives here is a regular user object with all the fields EXCEPT id!
    public void updateSeekerPref(@PathParam("id") int id, String seekerAsString) throws JSONException, Exception {
        JSONObject json = new JSONObject(seekerAsString);
        SeekerPreferences userPref = (SeekerPreferences) JSONObjectUtils.createSeekerPreferencesFromJsonObject(json);
        if (userPref == null) {
            String error = "Json object is invalid!";
            ErrorLogUtils.saveErrorLog(seekerAsString, error, "update seekerPref (json invalid)");
            ResourceUtils.errorResponse(response, error);
        } else {
            String error = userPrefActions.updateUserPreferences(id, userPref);
            if (error != null) {
                ErrorLogUtils.saveErrorLog(seekerAsString, error, "update seekerPref (error list is not empty)");
                ResourceUtils.errorResponse(response, error);
            } else {
                ResourceUtils.okResponse(response);
            }
        }
    }

    @POST
    @Path("/delete/{id}")
    public void deleteSeekerPrefById(@PathParam("id") int id) {
        boolean succeedToDelete = userPrefActions.deleteUserPreferencesById(id);
        if (!succeedToDelete) {
            ResourceUtils.errorResponse(response, "Could not delete preferences with user id " + id);
        } else {
            ResourceUtils.okResponse(response);
        }
    }
}
