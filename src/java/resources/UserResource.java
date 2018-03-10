package resources;

import com.google.gson.Gson;
import utils.ResourceUtils;
import pojos.User;
import dao.interfaces.IUserActions;
import dao.UserActions;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
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
import pojos.LogError;
import utils.ErrorLogUtils;
import utils.MySQLUtils;

@Path("/user")
public class UserResource {
    private final IUserActions userActions;
    
    @Context 
    HttpServletResponse response;

    public UserResource() {
        userActions = new UserActions();
    }

    @GET
    @Produces("application/json;charset=utf-8")
    public List<User> getAllUsers() {
        return userActions.getAllUsers();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public User getUserById(@PathParam("id") int id) {
        User user = userActions.findUserById(id);
        if (user == null) {
            ResourceUtils.errorResponse(response, "Could not find user with id " + id);
        }
        return user;
    }
    
    @GET
    @Path("/fb/{id}")
    @Produces("application/json;charset=utf-8")
    public User getUserByFacebookId(@PathParam("id") String fbId) {
        User user = userActions.findUserByFacebookId(fbId);
        if (user == null) {
            ResourceUtils.errorResponse(response, "Could not find user with Facebook id " + fbId);
        }
        return user;
    }

    @POST
    @Consumes("application/json;charset=utf-8")
    public void addNewUser(String jsonAsString) throws Exception {
        LogError error = new LogError();
        try {
            JSONObject jason = new JSONObject(jsonAsString);
            List<String> errors = userActions.createUser(jason);
            if (!errors.isEmpty()) {
                String errorsInJson = new Gson().toJson(errors);
                ErrorLogUtils.saveErrorLog(jsonAsString, errorsInJson, "Add new user (error list is not empty)");
            
                ResourceUtils.errorResponse(response, errorsInJson);
            } else {
            ResourceUtils.okResponse(response);
            }
        } catch (Exception ex) {
            ErrorLogUtils.saveErrorLog(jsonAsString, ex.getMessage(), "Add new user (exception occured)");
            ResourceUtils.errorResponse(response, ex.getMessage());
        }
    }

    @POST
    @Path("/{id}")
    @Consumes("application/json;charset=utf-8")
    // The User object that arrives here is a regular user object with all the fields EXCEPT id!
    public void updateUser(@PathParam("id") int id, User user) throws Exception {
        try{
        List<String> errors = userActions.updateUser(id, user);
        if (errors != null && !errors.isEmpty()) {
            ErrorLogUtils.saveErrorLog(user.toString(), errors.toString(), "update user");
            ResourceUtils.errorResponse(response, errors.toString());
        } else {
            ResourceUtils.okResponse(response);
        }
        } catch(Exception ex) {
            ErrorLogUtils.saveErrorLog(user.toString(), ex.getMessage(), "update user (exception occured)");
            ResourceUtils.errorResponse(response, ex.getMessage());

        }
    }

    @POST
    @Path("/delete/{id}")
    public void deleteUserById(@PathParam("id") int id) {
        boolean succeedToDelete = userActions.deleteUserById(id);
        if (!succeedToDelete) {
            ResourceUtils.errorResponse(response, "Could not delete user with id " + id);
        } else {
            ResourceUtils.okResponse(response);
        }
    }
}