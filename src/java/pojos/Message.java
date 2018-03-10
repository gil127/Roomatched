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
@Table(name=Configuration.Table.MESSAGES)
public class Message implements Serializable{
    @TableGenerator(name = "messageTableGenerator", 
    allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
    generator="messageTableGenerator")
    private long messageId;
    private String content;
    private int userSend;
    private long timeStamp;
    private int convId;
    
    public long getMessageId() {
        return messageId;
    }
    
    public void setMessageId(long value) {
        this.messageId = value;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String value) {
        this.content = value;
    }
    
    public int getUserSend() {
        return userSend;
    }
    
    public void setUserSend(int value) {
        this.userSend = value;
    }
    
    public long getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(long value) {
        this.timeStamp = value;
    }
    
    public int getConvId() {
        return convId;
    }
    
    public void setConvId(int value) {
        this.convId = value;
    }
    
    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        
        strBuilder.append("messageId: ").append(messageId).append(", convId: ").append(convId).append(", userSend: ")
                .append(userSend).append(", content: ").append(content).append(", timeStamp: ").append(timeStamp);
        
        return strBuilder.toString();
    }
}
