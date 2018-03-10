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
 * @author Gil
 */
@Entity
@Table(name = Configuration.Table.USERS_PREFF_SELECTION)
public class UsersPreffSelection implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "usersPreffSelectionTableGenerator",
            allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "usersPreffSelectionTableGenerator")
    private int type;
    private int smoking;
    private int sharedExpences;
    private int vegan;
    private int kosher;
    private int animals;
    private int musicianFriendly;
    private int gayFriendly;
    private int nightLife;

    public int getType() {
        return this.type;
    }

    public void setType(int value) {
        this.type = value;
    }

    public int getSmoking() {
        return this.smoking;
    }

    public void setsmoking(int value) {
        this.smoking = value;
    }

    public int getSharedExpences() {
        return this.sharedExpences;
    }

    public void setSharedExpences(int value) {
        this.sharedExpences = value;
    }

    public int getVegan() {
        return this.vegan;
    }

    public void setVegan(int value) {
        this.vegan = value;
    }

    public int getKosher() {
        return this.kosher;
    }

    public void setKosher(int value) {
        this.kosher = value;
    }

    public int getAnimals() {
        return this.animals;
    }

    public void setAnimals(int value) {
        this.animals = value;
    }

    public int getMusicianFriendly() {
        return this.musicianFriendly;
    }

    public void setMusicianFriendly(int value) {
        this.musicianFriendly = value;
    }

    public int getGayFriendly() {
        return this.gayFriendly;
    }

    public void setGayFriendly(int value) {
        this.gayFriendly = value;
    }

    public int getNightLife() {
        return this.nightLife;
    }

    public void setNightLife(int value) {
        this.nightLife = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) type;
        //for (Character ch : type.toCharArray()) {
        //    hash += (int) ch;
        //}
        
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UsersPreffSelection)) {
            return false;
        }
        UsersPreffSelection other = (UsersPreffSelection) object;
        if (this.type != other.type) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("type  = " + type);
        sb.append("\nsmoking = " + smoking);
        sb.append("\nsharedExpences = " + sharedExpences);
        sb.append("\nkosher = " + kosher);
        sb.append("\nvegan = " + vegan);
        sb.append("\nmusicianFriendly = " + musicianFriendly);
        sb.append("\ngayFriendly = " + gayFriendly);
        sb.append("\nnightLife = " + nightLife);
        return sb.toString();
    }
}
