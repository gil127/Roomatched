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
@Table(name=Configuration.Table.USER_SETTINGS)
public class UserSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "userSettingsTableGenerator", 
        allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator="userSettingsTableGenerator")
    private int userId;
    private boolean notifications;
    private int minShowMatch;
    private int minIncludeMatch;
    private String email;
    private long lastTimeMailSent;
    
    public long getLastTimeMailSent() {
        return lastTimeMailSent;
    }
    
    public void setLastTimeMailSent(long mailSentTime) {
        lastTimeMailSent = mailSentTime;
    }
    
    public boolean isNotifications() {
        return notifications;
    }

    public void setNotification(boolean notifications) {
        this.notifications = notifications;
    }
    
    public void setEmail(String mail) {
        this.email = mail;
    }
    
    public String getEmail() {
        return email;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getMinShowMatch() {
        return minShowMatch;
    }
    
    public void setMinShowMatch(int minShowMatch) {
        this.minShowMatch = minShowMatch;
    }
    
    public int getMinIncludeMatch() {
        return this.minIncludeMatch;
    }
    
    public void setMinIncludeMatch(int minIncludeMatch) {
        this.minIncludeMatch = minIncludeMatch;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserSettings)) {
            return false;
        }
        UserSettings other = (UserSettings) object;
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("userId = " + userId);
        sb.append("\nemail = " + email);
        sb.append("\nminIncludeMatch = " + this.minIncludeMatch);
        sb.append("\nminShowMatch = " + minShowMatch);
        return sb.toString();
    }    
}
