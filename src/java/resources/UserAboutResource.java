/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import dao.UserAboutActions;
import dao.interfaces.IUserAboutActions;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import utils.ResourceUtils;

/**
 *
 * @author Gil
 */
@Path("/userAbout")
public class UserAboutResource {
    @Context
    HttpServletResponse response;
    
    private final IUserAboutActions userAbout;

    public UserAboutResource() {
        userAbout = new UserAboutActions();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public void getUserAbout(@PathParam("id") int id) {        
        JSONObject res = userAbout.getUserAbout(id);
        if (res != null) {
            ResourceUtils.okResponse(response, res.toString());
        } else {
            ResourceUtils.errorResponse(response, "Error occurred while trying to read user " + id + "about field");
        }
    }

    @POST
    @Path("/{id}")
    @Consumes("application/json;charset=utf-8")
    public void updateUserAbout(@PathParam("id") int id, String aboutValue) throws Exception {
        try {
            JSONObject json = new JSONObject(aboutValue);
            String aboutAsString = json.optString("about", null);
            if (aboutAsString != null) {
                boolean succeed = userAbout.updateUserAbout(id, aboutAsString);
                if (succeed) {
                    ResourceUtils.okResponse(response);
                } else {
                    ErrorLogUtils.saveErrorLog(aboutValue, "could not update user about", "update userAbout (error list is not empty)");
                    ResourceUtils.errorResponse(response, "Error occurred while trying to update user " + id + " about field");
                }
            } else {
                ResourceUtils.errorResponse(response, "About field did not apeared in the json");
            }
        } catch (JSONException ex) {
            ErrorLogUtils.saveErrorLog(aboutValue, ex.getMessage(), "update user about (exception)");
            ResourceUtils.errorResponse(response, "json is illegal");
        }
    }
}
