/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import utils.ResourceUtils;
import dao.interfaces.IMatchesActions;
import dao.MatchesActions;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
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
@Path("/match")
public class MatchResources {
    private final IMatchesActions matchActions;
    
    @Context 
    HttpServletResponse response;

    public MatchResources() {
        matchActions = new MatchesActions();
    }
    
    @GET
    @Produces("application/json;charset=utf-8")
    @Path("/{id}")
    public void getAllMatches(@PathParam("id") int id) {
        List<JSONObject> MatchesList = matchActions.getAllMatches(id);
        if (MatchesList != null) {
            ResourceUtils.okResponse(response, MatchesList.toString());
        } else {
            ResourceUtils.errorResponse(response, "There is no user with the id: " + id);
        }
    }
}
