/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import configuration.Configuration;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author borisa
 */
@Entity
@Table(name=Configuration.Table.ROOM_DETAILS)
public class RoomDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "roomDetailsTableGenerator", 
        allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator="roomDetailsTableGenerator")
    private int roomId;
    private int apartmentId;
    private int price;
    private int size;
    private boolean hasBalcony;
    private boolean airConditioned;
    private boolean seperateBathroom;
    private boolean isFurnished;
    private String photoUrl;

    public int getId() {
        return roomId;
    }

    public void setId(int id) {
        this.roomId = id;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int roomSize) {
        this.size = roomSize;
    }

    public boolean getHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(boolean balconyIncluded) {
        this.hasBalcony = balconyIncluded;
    }

    public boolean getAirConditioned() {
        return airConditioned;
    }

    public void setAirConditioned(boolean airConditionerIncluded) {
        this.airConditioned = airConditionerIncluded;
    }

    public boolean getSeperateBathroom() {
        return seperateBathroom;
    }

    public void setSeperateBathroom(boolean seperateBathroomIncluded) {
        this.seperateBathroom = seperateBathroomIncluded;
    }

    public boolean getIsFurnished() {
        return isFurnished;
    }

    public void setIsFurnished(boolean furnitureIncluded) {
        this.isFurnished = furnitureIncluded;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) roomId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RoomDetails)) {
            return false;
        }
        RoomDetails other = (RoomDetails) object;
        if (this.roomId != other.roomId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {    
        StringBuilder sb = new StringBuilder();
        sb.append("roomId = " + roomId);
        sb.append("\n" + "apartmentId = " + apartmentId);
        sb.append("\n" + "price = " + this.price);
        sb.append("\n" + "size = " + this.size);
        sb.append("\n" + "hasBalcony = " + this.hasBalcony);
        sb.append("\n" + "airConditioned = " + this.airConditioned);
        sb.append("\n" + "seperateBathroom = " + this.seperateBathroom);
        sb.append("\n" + "isFurnished = " + this.isFurnished);
        sb.append("\n" + "photoUrl = " + this.photoUrl);

        return sb.toString();
    }    
    
}
