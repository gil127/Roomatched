/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import dao.MapsActions;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.json.JSONObject;
import pojos.LogError;
import utils.JSONObjectUtils;
import utils.MySQLUtils;
import utils.ResourceUtils;

/**
 *
 * @author or
 */
@Path("/errorLog")
public class ErrorLogResource {
    @Context 
    HttpServletResponse response;

    @GET
    @Path("/{days}")
    @Produces("application/json;charset=utf-8")
    public void getAllDistricts(@PathParam("days") int days) {
        long daysInMilli = getLastDaysInMilli(days);
        List errorsAsJson = MySQLUtils.runQuery("select e from " + LogError.class.getName() + " e where e.timeStampCol > " + daysInMilli);
        //List<JSONObject> errorsAsJson = JSONObjectUtils.getListOfJsonObjectFromErrors(errors);
        if (errorsAsJson == null || errorsAsJson.isEmpty()) {
            ResourceUtils.okResponse(response, "There is no errors in the last " + days + " days");
        } else {
            ResourceUtils.okResponse(response, errorsAsJson.toString());
        }
    }

    private long getLastDaysInMilli(int days) {
        return Calendar.getInstance().getTime().getTime() - days * 86_400_000;
    }
}
