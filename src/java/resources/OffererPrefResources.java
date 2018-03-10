/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import utils.ResourceUtils;
import pojos.interfaces.IUserPreferences;
import pojos.OffererPreferences;
import dao.interfaces.IPreferencesActions;
import dao.OffererPreferencesActions;
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
@Path("/offererPref")
public class OffererPrefResources {
    private final IPreferencesActions userPrefActions;
    
    @Context 
    HttpServletResponse response;

    public OffererPrefResources() {
        userPrefActions = new OffererPreferencesActions();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllOfferersPreferences() {
        List<IUserPreferences> allUserPreferences = userPrefActions.getAllUserPreferences();
        List<JSONObject> res = new ArrayList<JSONObject>();
        
        for (IUserPreferences userPref : allUserPreferences) {
            res.add(JSONObjectUtils.createJsonObjectFromOffererPreferences((OffererPreferences)userPref));
        }
        
        ResourceUtils.okResponse(response, res.toString());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getOffererPrefById(@PathParam("id") int id) {
        OffererPreferences userPref = (OffererPreferences)userPrefActions.findUserPreferencesById(id);
        JSONObject json = JSONObjectUtils.createJsonObjectFromOffererPreferences(userPref);
        if (userPref == null) {
            ResourceUtils.errorResponse(response, "Could not find preferences with user id " + id);
        } else {
            ResourceUtils.okResponse(response, json.toString());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addNewOffererPref(String offererAsString) throws JSONException {
        JSONObject json = new JSONObject(offererAsString);
        IUserPreferences userPref = JSONObjectUtils.createOffererPreferencesFromJsonObject(json);
        String error = userPrefActions.createUserPreferences((IUserPreferences) userPref);
        if (error == null) {
            ResourceUtils.okResponse(response);
        } else {
            ResourceUtils.errorResponse(response, error);
        }
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    // The User object that arrives here is a regular user object with all the fields EXCEPT id!
    public void updateOffererPref(@PathParam("id") int id, String offererAsString) throws JSONException, Exception {
        JSONObject json = new JSONObject(offererAsString);
        IUserPreferences userPref = JSONObjectUtils.createOffererPreferencesFromJsonObject(json);
        if (userPref == null) {
            String error = "Json object is invalid!";
            ErrorLogUtils.saveErrorLog(offererAsString, error, "update offererPref (json invalid)");
            ResourceUtils.errorResponse(response, error);
        } else {
            String error = userPrefActions.updateUserPreferences(id, userPref);
            if (error != null) {
                ErrorLogUtils.saveErrorLog(offererAsString, error, "update offererPref (error list is not empty)");
                ResourceUtils.errorResponse(response, error);
            } else {
                ResourceUtils.okResponse(response);
            }
        }
    }

    @POST
    @Path("/delete/{id}")
    public void deleteOffererPrefById(@PathParam("id") int id) {
        boolean succeedToDelete = userPrefActions.deleteUserPreferencesById(id);
        if (!succeedToDelete) {
            ResourceUtils.errorResponse(response, "Could not delete preferences with user id " + id);
        } else {
            ResourceUtils.okResponse(response);
        }
    }
}

