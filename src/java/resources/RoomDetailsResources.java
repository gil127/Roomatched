/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import utils.ResourceUtils;
import pojos.RoomDetails;
import dao.interfaces.IRoomDetailsActions;
import dao.RoomActions;
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
import pojos.LogError;
import utils.ErrorLogUtils;
import utils.MySQLUtils;

/**
 *
 * @author Gil
 */

@Path("/room")
public class RoomDetailsResources {
    private final IRoomDetailsActions roomDetailsActions;
    
    @Context 
    private HttpServletResponse response;
    
    public RoomDetailsResources() {
        roomDetailsActions = new RoomActions();
    }
    
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public RoomDetails getUserById(@PathParam("id") int id) {
        RoomDetails room = roomDetailsActions.findRoomDetailsById(id);
        if (room == null) {
            ResourceUtils.errorResponse(response, "Could not find room with room id " + id);
        }
        return room;
    }
    
    @POST
    @Consumes("application/json;charset=utf-8")
    public void addNewRoomDetails(RoomDetails room) {
        roomDetailsActions.createRoomDetails(room);
        ResourceUtils.okResponse(response);
    }

    @POST
    @Path("/{id}")
    @Consumes("application/json;charset=utf-8")
    public void updateRoomDetails(@PathParam("id") int id, RoomDetails room) throws Exception {
        try {
            List<String> errors = roomDetailsActions.updateRoomDetails(id, room);
            if (errors != null && !errors.isEmpty()) {
                ErrorLogUtils.saveErrorLog(room.toString(), errors.toString(), "Update roomDetails");
                ResourceUtils.errorResponse(response, errors.toString());
            } else {
                ResourceUtils.okResponse(response);
            }
        } catch (Exception ex) {
            ErrorLogUtils.saveErrorLog(room.toString(), ex.getMessage(), "Update roomDetails");
        }
    }

    @POST
    @Path("/delete/{id}")
    public void deleteRoomDetailsById(@PathParam("id") int id) {
        boolean succeedToDelete = roomDetailsActions.deleteRoomDetailsById(id);
        if (!succeedToDelete) {
            ResourceUtils.errorResponse(response, "Could not delete user with id " + id);
        } else {
            ResourceUtils.okResponse(response);
        }
    }
}
