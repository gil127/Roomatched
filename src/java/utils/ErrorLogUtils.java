/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Calendar;
import javax.persistence.EntityManager;
import pojos.LogError;

/**
 *
 * @author or
 */
public class ErrorLogUtils {

    public static void saveErrorLog(String json, String error, String action) throws Exception {
        LogError errorLog = new LogError();
        json = addLinesToJson(json);
        errorLog.setExceptionMsg(error);
        errorLog.setJson(json);
        errorLog.setTimeStampCol(Calendar.getInstance().getTime().getTime());
        errorLog.setAction(action);

        EntityManager em = MySQLUtils.getEntityManager();
        MySQLUtils.beginTransaction(em);
        MySQLUtils.persist(errorLog, em);
        MySQLUtils.commitTransaction(em);
    }

    private static String addLinesToJson(String json) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : json.toCharArray()) {
            sb.append(ch);
            if (ch.equals("{") || ch.equals("}") || ch.equals(",")) {
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }

}
