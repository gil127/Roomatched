/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import configuration.Configuration;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author or
 */
@Entity
@Table(name=Configuration.Table.ERROR)
public class LogError implements Serializable{
    @TableGenerator(name = "logErrorTableGenerator", 
    allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
    generator="logErrorTableGenerator")
    private long logId;
    
    private String json;
    private String exceptionMsg;
    private long timeStampCol;
    private String timeString;
    private String action;
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String value) {
        action = value;
    }
    
    public String getTimeString() {
        return timeString;
    }
    
    public void setTimeString(String value) {
        timeString = value;
    }
    
    public long getTimeStampCol() {
        return timeStampCol;
    }
    
    public void setTimeStampCol(long value) {
        timeStampCol = value;
        if (timeString == null || timeString.isEmpty())
            setTimeString(new Date(value).toString());
    }
    
    public long getLogId() {
        return logId;
    }
    
    public void setLogId(long value) {
        logId = value;
    }
    
    public String getJson() {
        return json;
    }
    
    public void setJson(String value) {
        json = value;
    }
    
    public String getExceptionMsg() {
        return exceptionMsg;
    }
    
    public void setExceptionMsg(String value) {
        exceptionMsg = value;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("logId = " + logId);
        sb.append("\n" + "action = " + this.action);
        sb.append("\n" + "json = " + this.json);
        sb.append("\n" + "exeptionMsg = " + this.exceptionMsg);
        sb.append("\n" + "time = " + this.timeString);

        return sb.toString();
    }
}
