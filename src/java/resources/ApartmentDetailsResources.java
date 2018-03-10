/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import utils.ResourceUtils;
import pojos.ApartmentDetails;
import dao.ApartmentActions;
import dao.interfaces.IApartmentDetailsActions;
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

/**
 *
 * @author Gil
 */
@Path("/apartment")
public class ApartmentDetailsResources {
 
    private final IApartmentDetailsActions apartmentDetailsActions;
    
    @Context 
    private HttpServletResponse response;
    
    public ApartmentDetailsResources() {
        apartmentDetailsActions = new ApartmentActions();
    }
    
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public ApartmentDetails getApartmentById(@PathParam("id") int id) {
        ApartmentDetails apartment = apartmentDetailsActions.findApartmentDetailsById(id);
        if (apartment == null) {
            ResourceUtils.errorResponse(response, "Could not find apartment with apartment id " + id);
        }
        return apartment;
    }
    
    @POST
    @Consumes("application/json;charset=utf-8")
    public void addNewApartmentDetails(ApartmentDetails apartment) {
        apartmentDetailsActions.createApartmentDetails(apartment);
        ResourceUtils.okResponse(response);
    }

    @POST
    @Path("/{id}")
    @Consumes("application/json;charset=utf-8")
    public void updateApartmentDetails(@PathParam("id") int id, ApartmentDetails apartment) throws Exception {
        List<String> errors = apartmentDetailsActions.updateApartmentDetails(id, apartment);
        if (errors != null && !errors.isEmpty()) {
            ErrorLogUtils.saveErrorLog(apartment.toString(), errors.toString(), "Update apartment");
            ResourceUtils.errorResponse(response, errors.toString());
        } else {
            ResourceUtils.okResponse(response);
        }
    }

    @POST
    @Path("/delete/{id}")
    public void deleteApartmentDetailsById(@PathParam("id") int id) {
        boolean succeedToDelete = apartmentDetailsActions.deleteApartmentDetailsById(id);
        if (!succeedToDelete) {
            ResourceUtils.errorResponse(response, "Could not delete apartment with id " + id);
        } else {
            ResourceUtils.okResponse(response);
        }
    }
}