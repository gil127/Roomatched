package resources;

import utils.ResourceUtils;
import pojos.UserSettings;
import dao.interfaces.IUserSettingActions;
import dao.UserSettingsActions;
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
import utils.ErrorLogUtils;

@Path("/settings")
public class UserSettingsResources {
    private final IUserSettingActions userSettingsActions;
    
    @Context 
    HttpServletResponse response;

    public UserSettingsResources() {
        userSettingsActions = new UserSettingsActions();
    }

    @GET
    @Produces("application/json;charset=utf-8")
    public List<UserSettings> getAllUsers() {
        return userSettingsActions.getAllUsersSettings();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public UserSettings getUserById(@PathParam("id") int id) {
        UserSettings settings = userSettingsActions.findUserSettingsById(id);
        if (settings == null) {
            ResourceUtils.errorResponse(response, "Could not find settings with user id " + id);
        }
        return settings;
    }

    @POST
    @Consumes("application/json;charset=utf-8")
    public void addNewUser(UserSettings settings) {
        userSettingsActions.createUserSettings(settings);
        ResourceUtils.okResponse(response);
    }

    @POST
    @Path("/{id}")
    @Consumes("application/json;charset=utf-8")
    public void updateUser(@PathParam("id") int id, UserSettings settings) throws Exception {
        try {
        List<String> errors = userSettingsActions.updateUserSettings(id, settings);
        if (errors != null && !errors.isEmpty()) {
            ErrorLogUtils.saveErrorLog(settings.toString(), errors.toString(), "Update user settings");
            ResourceUtils.errorResponse(response, errors.toString());
        } else {
            ResourceUtils.okResponse(response);
        }} catch (Exception ex) {
            ErrorLogUtils.saveErrorLog(settings.toString(), ex.getMessage(), "Update user settings");
            ResourceUtils.errorResponse(response, ex.getMessage());
        }
    }

    @POST
    @Path("/delete/{id}")
    public void deleteUserById(@PathParam("id") int id) {
        boolean succeedToDelete = userSettingsActions.deleteUserSettingsById(id);
        if (!succeedToDelete) {
            ResourceUtils.errorResponse(response, "Could not delete settings with user id " + id);
        } else {
            ResourceUtils.okResponse(response);
        }
    }
}
