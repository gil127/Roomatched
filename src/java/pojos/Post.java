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
@Table(name=Configuration.Table.POSTS)
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "postsTableGenerator", 
        allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator="postsTableGenerator")
    private int postId;
    private int offererId;
    private int roomId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int id) {
        this.postId = id;
    }

    public int getOffererId() {
        return offererId;
    }

    public void setOffererId(int offererId) {
        this.offererId = offererId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) postId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if (this.postId != other.postId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "core.tables.posts.Post[ id=" + postId + " ]";
    }
    
}
