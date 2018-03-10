package resources;

import pojos.AuthObject;
import utils.MySQLUtils;
import utils.ResourceUtils;
import pojos.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import utils.SessionUtils;

@Path("/auth")
public class AuthResource {    

    @Context 
    HttpServletResponse response;
    @Context
    HttpServletRequest request;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject isLoggedIn() throws IOException {
        JSONObject result = new JSONObject();
        HttpSession session;
        response.setContentType("application/json");

        session = request.getSession(false);
        try {
            if (session != null && (boolean) session.getAttribute(SessionUtils.IS_LOGGED_IN) == true) {
                    System.out.println("logged in!");
                    result.put("userId", session.getAttribute(SessionUtils.USER_ID))
                          .put("loggedIn", true)
                          .put("email", session.getAttribute(SessionUtils.USER_EMAIL))
                          .put("type", session.getAttribute(SessionUtils.USER_TYPE))
                          .put("firstName", session.getAttribute(SessionUtils.USER_FIRST_NAME))
                          .put("lastName", session.getAttribute(SessionUtils.USER_LAST_NAME))
                          .put("sex", session.getAttribute(SessionUtils.USER_SEX))
                          .put("userPhoto", session.getAttribute(SessionUtils.USER_PHOTO_URL));
            }
            else {
                System.out.println("not logged in!");
                // Report error in the form of an object to see whats wrong..
                result.put("loggedIn", false);
            }
                response.getWriter().print(result);
                response.flushBuffer();
        }
        catch (JSONException | IOException ex) {
            Logger.getLogger(AuthResource.class.getName()).log(Level.SEVERE, null, ex);
            ResourceUtils.errorResponse(response, null);
        }
        
        return result;
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject doLogin(AuthObject auth){
        JSONObject res = new JSONObject();
        
        String receivedEmail = auth.getEmail();
        String receivedPassword = auth.getPassword();
        
        User user = MySQLUtils.getUserByMailAndPass(User.class, receivedEmail, receivedPassword);
        
        try {
            if (receivedEmail.equals(user.getEmail()) && receivedPassword.equals((user.getPassword()))) {
                HttpSession session = request.getSession();
                session.setAttribute(SessionUtils.USER_ID, user.getId());
                session.setAttribute(SessionUtils.USER_EMAIL, user.getEmail());
                session.setAttribute(SessionUtils.USER_TYPE, user.getType());
                session.setAttribute(SessionUtils.USER_FIRST_NAME, user.getFirstName());
                session.setAttribute(SessionUtils.USER_LAST_NAME, user.getLastName());
                session.setAttribute(SessionUtils.IS_LOGGED_IN, true);
                session.setAttribute(SessionUtils.USER_SEX, user.getSex());
                session.setAttribute(SessionUtils.USER_PHOTO_URL, user.getPhotoUrl());
            
                // for saving AJAX request we can return JsonObject with auth details
                 res.put("userId", user.getId());
                 res.put("loggedIn", true);
                 res.put("email", user.getEmail());
                 res.put("type", user.getType());
                 res.put("firstName", user.getFirstName());
                 res.put("lastName", user.getLastName());
                 res.put("sex", user.getSex());
                 res.put("photoUrl", user.getPhotoUrl());
                 
                ResourceUtils.okResponse(response, res.toString());
        }
            else {
                ResourceUtils.errorResponse(response, "Failed to login. Wrong email / password entered");
                res.put("loggedIn", false);
            }
        } catch(JSONException ex) {
            Logger.getLogger(AuthResource.class.getName()).log(Level.SEVERE, null, ex);
            ResourceUtils.errorResponse(response, null);
        }
        
        return res;
    }
    
    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public void doLogout(){
        if (request.getSession(false) != null) {
             request.getSession(false).setAttribute(SessionUtils.IS_LOGGED_IN, false);
         }
    }
}
