/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Gil
 */
public interface IMatchesActions {
    public List<JSONObject> getAllMatches(int id);
    public JSONObject getOffererMatchDetailsByOffererAndSeeker(int seekerId, int offererId);
    public JSONObject getSeekerMatchDetailsByOffererAndSeeker(int seekerId, int offererId);
}
