/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author borisa
 */
public class ResourceUtils {
    public static void errorResponse(HttpServletResponse response, String message) {
       try {
           JSONObject json = new JSONObject();
           response.setStatus(500);
            response.setContentType("application/json");
           response.getWriter().write(json.put("error", message).toString());
           response.flushBuffer();
       } catch (IOException | JSONException ex) {
           Logger.getLogger(ResourceUtils.class.getName()).log(Level.SEVERE, null, ex);
       }   
    }
    
    // If you do not return a json with information (for example - adding a new user will not return a json..) use this to
    // let me know that the action was successfull. This sets response code 200.
    public static void okResponse(HttpServletResponse response) {
        try {
           response.setStatus(200);
           response.flushBuffer();
        } catch (IOException ex) {
           Logger.getLogger(ResourceUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // If you want to return complex / custom JSON content, instead of just a POJO (like in the case of posts..)
    public static void okResponse(HttpServletResponse response, String content) {
        if (content == null || content.isEmpty()) {
            okResponse(response);
        } else {
            try {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(content);
                okResponse(response);
            } catch (IOException ex) {
                Logger.getLogger(ResourceUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
