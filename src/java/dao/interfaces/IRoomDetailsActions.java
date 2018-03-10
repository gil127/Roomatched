/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import pojos.RoomDetails;
import java.util.List;

/**
 *
 * @author Gil
 */
public interface IRoomDetailsActions {
    public boolean deleteRoomDetailsById(int id);
    public List<String> updateRoomDetails(int roomId, RoomDetails room);
    public void createRoomDetails(RoomDetails room);
    public RoomDetails findRoomDetailsById(int id);
    public List<RoomDetails> getAllRooms();
}
