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
@Table(name=Configuration.Table.APARTMENT_DETAILS)
public class ApartmentDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "apartmentDetailsTableGenerator", 
        allocationSize = 1, initialValue = 100)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator="apartmentDetailsTableGenerator")
    private int apartmentId;
    private int offererUserId;
    private String city;
    private String address;
    private int size;
    private int floor;
    private int totalRooms;
    private boolean hasLivingRoom;
    private boolean hasElevator;
    private boolean isRenovated;
    private boolean hasParking;
    private int totalRoomates;
    private int approximateExpences;
    private String guarantees;
    private int freeRooms;
    private String photoUrl;
    private boolean isFurnished;
    
    public boolean getIsFurnished() {
        return isFurnished;
    }
    
    public void setIsFurnished(boolean value) {
        isFurnished = value;
    }
    
    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int id) {
        this.apartmentId = id;
    }

    public int getOffererUserId() {
        return offererUserId;
    }

    public void setOffererUserId(int offererUserId) {
        this.offererUserId = offererUserId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int apartmentSize) {
        this.size = apartmentSize;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int apartmentFloor) {
        this.floor = apartmentFloor;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public boolean getHasLivingRoom() {
        return hasLivingRoom;
    }

    public void setHasLivingRoom(boolean livingRoomIncluded) {
        this.hasLivingRoom = livingRoomIncluded;
    }

    public boolean getHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(boolean elevatorIncluded) {
        this.hasElevator = elevatorIncluded;
    }

    public boolean getIsRenovated() {
        return isRenovated;
    }

    public void setIsRenovated(boolean renovated) {
        this.isRenovated = renovated;
    }

    public boolean getHasParking() {
        return hasParking;
    }

    public void setHasParking(boolean parkingIncluded) {
        this.hasParking = parkingIncluded;
    }

    public int getTotalRoomates() {
        return totalRoomates;
    }

    public void setTotalRoomates(int numOfRoomates) {
        this.totalRoomates = numOfRoomates;
    }

    public int getApproximateExpences() {
        return approximateExpences;
    }

    public void setApproximateExpences(int billExpenses) {
        this.approximateExpences = billExpenses;
    }

    public String getGuarantees() {
        return guarantees;
    }

    public void setGuarantees(String guarantees) {
        this.guarantees = guarantees;
    }

    public int getFreeRooms() {
        return freeRooms;
    }

    public void setFreeRooms(int numOfAvailableRooms) {
        this.freeRooms = numOfAvailableRooms;
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
        hash += (int) apartmentId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApartmentDetails)) {
            return false;
        }
        ApartmentDetails other = (ApartmentDetails) object;
        if (this.apartmentId != other.apartmentId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {    
        StringBuilder sb = new StringBuilder();
        
        sb.append("offererUserId = " + offererUserId);
        sb.append("\n" + "apartmentId = " + apartmentId);
        sb.append("\n" + "city = " + this.city);
        sb.append("\n" + "size = " + this.size);
        sb.append("\n" + "address = " + this.address);
        sb.append("\n" + "floor = " + this.floor);
        sb.append("\n" + "totalRooms = " + this.totalRooms);
        sb.append("\n" + "hasLivingRoom = " + this.hasLivingRoom);
        sb.append("\n" + "hasElevator = " + this.hasElevator);
        sb.append("\n" + "isRenovated = " + this.isRenovated);
        sb.append("\n" + "hasParking = " + this.hasParking);
        sb.append("\n" + "isFurnished = " + this.isFurnished);
        sb.append("\n" + "totalRoomates = " + this.totalRoomates);
        sb.append("\n" + "approximateExpences = " + this.approximateExpences);
        sb.append("\n" + "guarantees = " + this.guarantees);
        sb.append("\n" + "freeRooms = " + this.freeRooms);
        sb.append("\n" + "photoUrl = " + this.photoUrl);

        return sb.toString();
    }    
    
}
