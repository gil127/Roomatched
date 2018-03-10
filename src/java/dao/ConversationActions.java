/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import configuration.Configuration;
import dao.interfaces.IConversationActions;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import pojos.Conversation;
import utils.MySQLUtils;
import java.util.Calendar;
import org.json.JSONObject;
import pojos.Message;
import pojos.MessageClient;
import pojos.User;
import pojos.UserSettings;
import utils.JSONObjectUtils;
import utils.MailUtils;
import utils.QueriesUtils;

/**
 *
 * @author Gil
 */
public class ConversationActions implements IConversationActions {

    @Override
    public List<JSONObject> getHistoryBetweenUsers(int firstUserId, int secondUserId, int messageId) throws Exception {
        String query = QueriesUtils.createQueryForConversationIdByUsers(firstUserId, secondUserId);
        List usersConversation = MySQLUtils.runQuery(query);

        if (usersConversation.size() > 1) {
            throw new Exception("There are two conversations or more for the same users");
        } else if (usersConversation.isEmpty()) {
            return new ArrayList<JSONObject>(); // Return an empty list - its ok.
        }
        
        int convId = (Integer) usersConversation.get(0);
        
        return getHistoryBetweenUsers(convId, messageId);
    }
    
    @Override
    public List<JSONObject> getHistoryBetweenUsersByConversationId(int conversationId, int lastMessage){
        return getHistoryBetweenUsers(conversationId, lastMessage);
    }
    
    private List<JSONObject> getHistoryBetweenUsers(int conversationId, int lastMessage) {
        String secondQuery = QueriesUtils.createQueryToPullUsersHistory(conversationId, lastMessage);
        List usersHistory = MySQLUtils.runQuery(secondQuery);

        List messages = convertMessageToClientFormat(usersHistory);
        
        return getJsonListFromListOfMessages(messages);
    }
    
    @Override
    public String addNewMessage(MessageClient msgClient) throws Exception {
        if (!checkValidityOfUserIds(msgClient)) {
            return "The userIDs is invalid";
        }
        
        int convId = getConversationId(msgClient);

        if (convId == Configuration.NO_VALUE) {
            return "There are 2 conversations with the same details";
        } else if (convId == 0) {
            return "There are a problem to insert new Conversation into the DB";
        }

        Message msg = new Message();

        msg.setConvId(convId);
        msg.setUserSend(msgClient.getFrom());
        msg.setContent(msgClient.getContent());
        msg.setTimeStamp(Calendar.getInstance().getTime().getTime());

        EntityManager em = MySQLUtils.getEntityManager();
        MySQLUtils.beginTransaction(em);
        MySQLUtils.persist(msg, em);
        String error = MySQLUtils.commitTransaction(em);
        
        if (error == null) {
            sendEmailToReciever(msgClient);
        }
        
        return error;
    }

    @Override
    public List<JSONObject> getAllUserConversation(int userId) {
        String query = QueriesUtils.createQueryForConversationByUser(userId);
        List allConversation = MySQLUtils.runQuery(query);
        
        List<Conversation> conversations = new ArrayList<Conversation>();
        
        for (Object obj : allConversation) {
            conversations.add((Conversation) obj);
        }
        
        if (conversations.isEmpty()) {
            return null;
        } else {
            return getJsonListFromListOfConv(conversations, userId);
        }
    }
    
    private List<JSONObject> getJsonListFromListOfConv(List<Conversation> list, int userId) {
        List<JSONObject> res = new ArrayList<JSONObject>();
        
        for (Conversation conv : list) {
            int otherUserId = conv.getUser_one() == userId? conv.getUser_two() : conv.getUser_one();
            User otherUser = (User) MySQLUtils.retrieveById(User.class, otherUserId);
            res.add(JSONObjectUtils.getJsonObjectFromConv(conv, otherUser));
        }
        
        return res;
    }

    private int getConversationId(MessageClient msgClient) throws Exception {
        if (msgClient.getFrom() == msgClient.getTo())
            return 0;
        
        String query = QueriesUtils.createQueryForConversationIdByUsers(msgClient.getFrom(), msgClient.getTo());
        List allConversation = MySQLUtils.runQuery(query);

        int convId;

        if (allConversation == null || allConversation.isEmpty()) {
            EntityManager em = MySQLUtils.getEntityManager();
            Conversation conv = new Conversation();
            conv.setUser_one(msgClient.getFrom());
            conv.setUser_two(msgClient.getTo());

            MySQLUtils.beginTransaction(em);

            conv = (Conversation) MySQLUtils.persist(conv, em);
            String error = MySQLUtils.commitTransaction(em);

            if (error == null)
                convId = conv.getConversationId();
            else {
                convId = 0;
            }
        } else if (allConversation.size() == 1) {
            convId = (int) allConversation.get(0);
        } else {
            convId = Configuration.NO_VALUE;
        }

        return convId;
    }

    private List<MessageClient> convertMessageToClientFormat(List<Message> usersHistory) {
        List<MessageClient> res = new ArrayList<>();

        for (Message msg : usersHistory) {
            MessageClient currMsgClient = new MessageClient();
            currMsgClient.setContent(msg.getContent());
            currMsgClient.setFrom(msg.getUserSend());
            currMsgClient.setMessageId(msg.getMessageId());
            Conversation conv = getConversationById(msg.getConvId());
            int tmpTo = msg.getUserSend() == conv.getUser_one() ? conv.getUser_two() : conv.getUser_one();
            currMsgClient.setTo(tmpTo);
            
            currMsgClient.setTimeStamp(msg.getTimeStamp());

            res.add(currMsgClient);
        }

        return res;
    }

    @Override
    public Conversation getConversationById(int convId) {
        Conversation conv = (Conversation) MySQLUtils.retrieveById(Conversation.class, convId);
        if (conv != null) {
            return conv;
        } else {
            return null;
        }
    }

    private boolean checkValidityOfUserIds(MessageClient msgClient) {
        User user1 = (User) MySQLUtils.retrieveById(User.class, msgClient.getFrom());
        User user2 = (User) MySQLUtils.retrieveById(User.class, msgClient.getTo());
        
        return user1 != null && user2 != null;
    }

    private List<JSONObject> getJsonListFromListOfMessages(List<MessageClient> messages) {
        List<JSONObject> res = new ArrayList<JSONObject>();
        
        for (MessageClient msg : messages) {
            res.add(JSONObjectUtils.getJsonObjectFromMsg(msg));
        }
        
        return res;
    }

    private void sendEmailToReciever(MessageClient msgClient) {
        UserSettingsActions settingsActions = new UserSettingsActions();
        UserSettings recipientUserSettings = settingsActions.findUserSettingsById(msgClient.getTo());
        String recipientUserEmail = recipientUserSettings.getEmail();
        
        if (settingsActions.checkIfMailNeedToBeSendToUser(recipientUserSettings)) {
            User recipientUser = (User) MySQLUtils.retrieveById(User.class, msgClient.getTo());
            String recipientName = recipientUser.getFirstName() + " " + recipientUser.getLastName();

            User fromUser = (User) MySQLUtils.retrieveById(User.class, msgClient.getFrom());
            String senderName = fromUser.getFirstName() + " " + fromUser.getLastName();
            
            String header = "Hello " + recipientName + ",";
            
            String htmlContent = createHtmlContent(senderName, msgClient.getContent(), header);
            
            boolean mailHasSent = MailUtils.sendTo(senderName, recipientName, recipientUserEmail, "You got new message in Roomatched ", htmlContent, true);
            
            if (mailHasSent) {
                recipientUserSettings.setLastTimeMailSent(Calendar.getInstance().getTime().getTime());
                settingsActions.updateUserSettings(recipientUserSettings.getUserId(), recipientUserSettings);
            }
        }
    }
    
    private String createHtmlContent(String senderName, String content, String header) {
        String mailContent = senderName + " has sent you a message:\n\"" + content + "\"";
        String sign = "\nRegards, \nRoomatched Team";
        StringBuilder sb = new StringBuilder();

        sb.append("<div style=\"white-space: pre; padding: 10px; background-color: #f9f9f9; color: #EB5635; border: 1px solid #EB5635; border-radius: 4px; font-size: 16px; \">");
        sb.append("<h2 style=\"font-size: 21px; font-color: #EB5635\">").append(header).append("</h2>");
        sb.append("<div style=\"font-color: #EB5635\">").append(mailContent).append("</div>");
        sb.append("<a style=\"display: block; margin: 10px 0; color: #EB5635;\" href=\"http://www.roomatched.com\">View on Roomatched</a>");
        sb.append("<div>").append(sign).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }
}
