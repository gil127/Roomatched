/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import dao.ConversationActions;
import dao.interfaces.IConversationActions;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
import pojos.Conversation;
import pojos.MessageClient;
import pojos.User;
import pojos.UserSettings;
import utils.ErrorLogUtils;
import utils.MailUtils;
import utils.MySQLUtils;
import utils.ResourceUtils;

/**
 *
 * @author Gil
 */
@Path("/messages")
public class MessagesResource {

    private final IConversationActions conversationActions;

    @Context
    HttpServletResponse response;

    public MessagesResource() {
        conversationActions = new ConversationActions();
    }

    @POST
    @Consumes("application/json;charset=utf-8")
    public void addNewMessage(MessageClient msgClient) throws Exception {
        String error = conversationActions.addNewMessage(msgClient);
        
        if (error != null) {
            ErrorLogUtils.saveErrorLog(msgClient.toString(), error, "Add new message");
            ResourceUtils.errorResponse(response, error);
        }
    }
    
    @GET
    @Produces("application/json;charset=utf-8")
    @Path("/{id}")
    public void getAllUserConversation(@PathParam("id") int userId) {
        List<JSONObject> allUserConversation = conversationActions.getAllUserConversation(userId);
        
        if (allUserConversation != null && !allUserConversation.isEmpty()) {
            ResourceUtils.okResponse(response, allUserConversation.toString());
        } else {
            ResourceUtils.errorResponse(response, "There are no conversation for the user with the id: " + userId);
        }
    }

    @GET
    @Produces("application/json;charset=utf-8")
    @Path("/history")
    public void getConversationContent(@QueryParam("id1") int firstUserId, @QueryParam("id2") int secondUserId, 
            @QueryParam("lastMessage") int messageId) throws Exception{
        List<JSONObject> historyMessages = conversationActions.getHistoryBetweenUsers(firstUserId, secondUserId, messageId);
        
        if (historyMessages != null) {
          ResourceUtils.okResponse(response, historyMessages.toString());
        } else {
            ResourceUtils.errorResponse(response, 
                    "There are no mesages for the users with the ids: " + firstUserId + ", " + secondUserId);
        }
    }
    
    @GET
    @Produces("application/json;charset=utf-8")
    @Path("/conversation")
    public void getConversationContentByConvId(@QueryParam("convId") int convId, @QueryParam("lastMessage") int messageId) {
        List<JSONObject> historyMessages = conversationActions.getHistoryBetweenUsersByConversationId(convId, messageId);
        
        if (historyMessages != null){
          ResourceUtils.okResponse(response, historyMessages.toString());
        } else {
            ResourceUtils.errorResponse(response, 
                    "There are no mesages for the conversationId: " + convId);
        }
    }
}