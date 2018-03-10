/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import java.util.List;
import org.json.JSONObject;
import pojos.Conversation;
import pojos.Message;
import pojos.MessageClient;

/**
 *
 * @author Gil
 */
public interface IConversationActions {
    List<JSONObject> getHistoryBetweenUsers(int firstUserId, int secondUserId, int messageId) throws Exception;
    List<JSONObject> getHistoryBetweenUsersByConversationId(int conversationId, int messageId);
    List<JSONObject> getAllUserConversation(int userId);
    String addNewMessage(MessageClient msgClient) throws Exception;
    Conversation getConversationById(int convId);
}