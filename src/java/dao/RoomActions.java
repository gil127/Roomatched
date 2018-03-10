package dao;

import dao.interfaces.IRoomDetailsActions;
import utils.MySQLUtils;
import pojos.RoomDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.json.JSONException;
import org.json.JSONObject;

public class RoomActions implements IRoomDetailsActions{
    public static List<String> checkValidation(RoomDetails room) {
        List<String> errors = new ArrayList<String>();
        
        if (room != null) {
            if(room.getPrice() <= 0){
                errors.add("The size should be non negative number");
            }
        } else {
            errors.add("The json object of the roomDetails is incorrect.");
        }
        
        return errors;
    }
    
    @Override
    public RoomDetails findRoomDetailsById(int id) {
        RoomDetails room = (RoomDetails) MySQLUtils.retrieveById(RoomDetails.class ,id);
        if (room != null) {
            return room;
        } else {
            return null;
        } 
    }

    @Override
    public boolean deleteRoomDetailsById(int id) {
        RoomDetails room = (RoomDetails) MySQLUtils.retrieveById(RoomDetails.class, id);
        if (room != null) {
            MySQLUtils.delete(room);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> updateRoomDetails(int id, RoomDetails room) {
        List<String> errors = checkValidation(room);
        
        if (errors != null && !errors.isEmpty()){
            return errors;
        }
        
        RoomDetails roomDetailsFromDB = (RoomDetails) MySQLUtils.retrieveById(RoomDetails.class, id);
        if (roomDetailsFromDB != null) {
            room.setId(roomDetailsFromDB.getId());
            MySQLUtils.Update(room);
            return null;
        } else {
            errors = new ArrayList<String>();
            errors.add("There is no roomDetails with roomId " + id);
            return errors;
        }
    }

    @Override
    public void createRoomDetails(RoomDetails room) {
        try {
            EntityManager em = MySQLUtils.getEntityManager();
            MySQLUtils.beginTransaction(em);
            MySQLUtils.persist(room, em);
            MySQLUtils.commitTransaction(em);
        } catch (Exception ex) {
            Logger.getLogger(RoomActions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<RoomDetails> getAllRooms() {
        List<RoomDetails> rooms = (List<RoomDetails>)(List<?>) MySQLUtils.retrieveAll(RoomDetails.class);
        if (rooms.isEmpty()) {
            return null;
        } else {
            return rooms;
        } 
    }
}
