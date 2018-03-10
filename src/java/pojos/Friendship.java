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
@Table(name = Configuration.Table.FRIENDSHIP)
public class Friendship implements Serializable {
    @TableGenerator(name = "friendshipTableGenerator", 
    allocationSize = 1, initialValue = 1)
    @Id   
    @GeneratedValue(strategy = GenerationType.TABLE,
    generator="friendshipTableGenerator")
    private long friendshipId;
    
    private String userOnefacebookID;
    private String userTwoFacebookID;
    
    public long getFriendshipId() {
        return friendshipId;
    }
    
    public void setFriendshipId(long value) {
        friendshipId = value;
    }
    
    public String getUserOnefacebookID() {
        return this.userOnefacebookID;
    }
    
    public void setUserOnefacebookID(String value) {
        this.userOnefacebookID = value;
    }
    
    public String getUserTwofacebookID() {
        return this.userTwoFacebookID;
    }
    
    public void setUserTwoFacebookID(String value) {
        this.userTwoFacebookID = value;
    }

}
