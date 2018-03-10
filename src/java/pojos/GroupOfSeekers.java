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
@Table(name=Configuration.Table.GROUP_OF_SEEKERS)
public class GroupOfSeekers implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name = "groupOfSeekersTableGenerator", 
        allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator="groupOfSeekersTableGenerator")
    private int groupId;
    private int seeker1Id;
    private int seeker2Id;
    private int seeker3Id;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int id) {
        this.groupId = id;
    }

    public int getSeeker1Id() {
        return seeker1Id;
    }

    public void setSeeker1Id(int seeker1Id) {
        this.seeker1Id = seeker1Id;
    }

    public int getSeeker2Id() {
        return seeker2Id;
    }

    public void setSeeker2Id(int seeker2Id) {
        this.seeker2Id = seeker2Id;
    }

    public int getSeeker3Id() {
        return seeker3Id;
    }

    public void setSeeker3Id(int seeker3Id) {
        this.seeker3Id = seeker3Id;
    }

    @Override
    public String toString() {
        return "core.tables.groupOfSeekers.GroupOfSeekers[ id=" + groupId + " ]";
    }
    
}
