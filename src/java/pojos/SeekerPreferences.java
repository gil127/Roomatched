/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import pojos.interfaces.IUserPreferences;
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
@Table(name = Configuration.Table.SEEKER_PREF)
public class SeekerPreferences implements Serializable, IUserPreferences {

    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "seekerPrefTableGenerator",
            allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "seekerPrefTableGenerator")
    private int userId;
    private String address;
    private String city;
    private int minPricePreffered;
    private int maxPricePreffered;
    private int numberOfRoomates;
    private int smoking;
    private String seekerOccupation;
    private int sharedExpences;
    private int vegan;
    private int kosher;
    private int animals;
    private int musicianFriendly;
    private int gayFriendly;
    private int nightLife;
    private String firstValuablePref;
    private String secondValuablePref;
    private String thirdValuablePref;

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int id) {
        this.userId = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getMinPricePreffered() {
        return minPricePreffered;
    }

    public void setMinPricePreffered(int pricePreferred) {
        this.minPricePreffered = pricePreferred;
    }

    public int getMaxPricePreffered() {
        return maxPricePreffered;
    }

    public void setMaxPricePreffered(int pricePreferred) {
        this.maxPricePreffered = pricePreferred;
    }

    public int getNumberOfRoomates() {
        return numberOfRoomates;
    }

    public void setNumberOfRoomates(int numOfRoomates) {
        this.numberOfRoomates = numOfRoomates;
    }

    @Override
    public int getSmoking() {
        return smoking;
    }

    @Override
    public void setSmoking(int smoking) {
        this.smoking = smoking;
    }

    public String getSeekerOccupation() {
        return seekerOccupation;
    }

    public void setSeekerOccupation(String occupation) {
        this.seekerOccupation = occupation;
    }

    @Override
    public int getSharedExpences() {
        return sharedExpences;
    }

    @Override
    public void setSharedExpences(int sharedExpenses) {
        this.sharedExpences = sharedExpenses;
    }

    @Override
    public int getVegan() {
        return vegan;
    }

    @Override
    public void setVegan(int vegeterian) {
        this.vegan = vegeterian;
    }

    @Override
    public int getKosher() {
        return kosher;
    }

    @Override
    public void setKosher(int kosherFood) {
        this.kosher = kosherFood;
    }

    @Override
    public int getAnimals() {
        return animals;
    }

    @Override
    public void setAnimals(int pets) {
        this.animals = pets;
    }

    @Override
    public int getGayFriendly() {
        return gayFriendly;
    }

    @Override
    public void setGayFriendly(int value) {
        gayFriendly = value;
    }

    @Override
    public int getMusicianFriendly() {
        return musicianFriendly;
    }

    @Override
    public void setMusicianFriendly(int value) {
        musicianFriendly = value;
    }
    
    @Override
    public int getNightLife() {
        return this.nightLife;
    }
    
    @Override
    public void setNightLife(int value) {
        this.nightLife = value;
    }

    @Override
    public String getFirstValuablePref() {
        return this.firstValuablePref;
    }

    @Override
    public void setFirstValuablePref(String value) {
        this.firstValuablePref = value;
    }

    @Override
    public String getSecondValuablePref() {
        return this.secondValuablePref;
    }

    @Override
    public void setSecondValuablePref(String value) {
        this.secondValuablePref = value;
    }

    @Override
    public String getThirdValuablePref() {
        return this.thirdValuablePref;
    }

    @Override
    public void setThirdValuablePref(String value) {
        this.thirdValuablePref = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SeekerPreferences)) {
            return false;
        }
        SeekerPreferences other = (SeekerPreferences) object;
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.tables.seekerPref.SeekerPreferences[ id=" + userId + " ]";
    }
}
