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
@Table(name=Configuration.Table.CONVERSATION)
public class Conversation implements Serializable{
    @TableGenerator(name = "conversationTableGenerator", 
    allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
    generator="conversationTableGenerator")
    private int conversationId;
    private int user_one;
    private int user_two;

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int value) {
        this.conversationId = value;
    }
    
    public int getUser_one() {
        return user_one;
    }
    
    public void setUser_one(int value) {
        this.user_one = value;
    }
    
    public int getUser_two() {
        return user_two;
    }
    
    public void setUser_two(int value) {
        this.user_two = value;
    }
    
    
    @Override
    public String toString() {
        return "conversationId:" + conversationId + ", user_one:" + user_one + ", user_two:" + user_two;
    }
}
