/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import dao.MapsActions;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import utils.ResourceUtils;

/**
 *
 * @author or
 */
@Path("/maps")
public class MapsResource {
    private MapsActions actions;
    
    @Context 
    HttpServletResponse response;

    public MapsResource() {
        actions = new MapsActions();
    }
    
    @GET
    @Path("/districts")
    @Produces("application/json;charset=utf-8")
    public void getAllDistricts() {
        List<JSONObject> allDistricts = actions.getAllDistricts();
        if (allDistricts == null || allDistricts.isEmpty()) {
            ResourceUtils.errorResponse(response, "Cannot find districts");
        } else {
            ResourceUtils.okResponse(response, allDistricts.toString());
        }
    }
    
    @GET
    @Path("/cities")
    @Produces("application/json;charset=utf-8")
    public void getAllCities(@QueryParam("district") String district) {
        List<JSONObject> allCities = actions.getCitiesByDistrict(district);
        if (allCities == null || allCities.isEmpty()) {
            ResourceUtils.errorResponse(response, "Cannot find cities");
        } else {
            ResourceUtils.okResponse(response, allCities.toString());
        }
    }
    
    @GET
    @Path("/city")
    @Produces("application/json;charset=utf-8")
    public void getAllNeighberhoodByCity(@QueryParam("city") String city) {
        List<JSONObject> result = actions.getNeighberhoodByCity(city);
        if (result == null || result.isEmpty()) {
            ResourceUtils.errorResponse(response, "Cannot find city");
        } else {
            ResourceUtils.okResponse(response, result.toString());
        }
    }
}
