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
@Table(name=Configuration.Table.FAVORITES)
public class Favorite implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "favoriteTableGenerator", 
        allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator="favoriteTableGenerator")
    private int favoriteId;
    private int userId;
    private int matchingUserId;

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int id) {
        this.favoriteId = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMatchingUserId() {
        return matchingUserId;
    }

    public void setMatchingUserId(int matchingUserId) {
        this.matchingUserId = matchingUserId;
    }
    
    @Override
    public String toString() {
        return "core.tables.Favorites.favorite[ id=" + favoriteId + " ]";
    }
    
}
