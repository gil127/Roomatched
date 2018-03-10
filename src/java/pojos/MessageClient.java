/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Gil
 */
public class MessageClient {

    private String content;
    private int from;
    private int to;
    private long timeStamp;
    private long messageId;

    public String getContent() {
        return content;
    }
    
    public void setContent(String value) {
        this.content = value;
    }
    
    public int getFrom() {
        return from;
    }
    
    public void setFrom(int value) {
        this.from = value;
    }
    
    public long getMessageId() {
        return messageId;
    }
    
    public void setMessageId(long value) {
        this.messageId = value;
    }
    
    public int getTo() {
        return to;
    }
    
    public void setTo(int value) {
        this.to = value;
    }
    
    public long getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(long value) {
        this.timeStamp = value;
    }
    
}
