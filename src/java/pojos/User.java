/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import configuration.Configuration;
import java.io.Serializable;
import java.sql.Date;
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
@Table(name=Configuration.Table.USERS)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "usersTableGenerator", 
            allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator="usersTableGenerator")
    private int id;
    private String facebookId;
    
    private String password;
    private String type;
    private String email;
    private String firstName;
    private String lastName;
    private String sex;
    private int yearOfBirth;
    private String photoUrl;
    private String about;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getFacebookId() {
        return facebookId;
    }
    
    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
    
    public String getAbout() {
        return this.about;
    }
    
    public void setAbout(String value) {
        this.about = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        
        if (this.id == Configuration.NO_VALUE || other.id == Configuration.NO_VALUE) {
            // id's equal to -1 stands for uncomplete id
            return false;
        }
        
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("userId = " + id);
        sb.append("\nfirstName = " + firstName);
        sb.append("\nlastName = " + this.lastName);
        sb.append("\nabout = " + this.about);
        sb.append("\nfacebookId = " + this.facebookId);
        sb.append("\nsex = " + this.sex);
        sb.append("\ntype = " + this.type);
        return sb.toString();
    }    
    
}
