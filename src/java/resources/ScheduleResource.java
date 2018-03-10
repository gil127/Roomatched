/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import UnitTests.UnitTestsResources;
import dao.MatchesActions;
import dao.UserActions;
import dao.interfaces.IMatchesActions;
import dao.interfaces.IUserActions;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.ApartmentDetails;
import pojos.User;
import pojos.MatchingUser;
import pojos.RoomDetails;
import pojos.UserSettings;
import utils.MailUtils;
import utils.MySQLUtils;
import utils.QueriesUtils;

/**
 * @author Gil
 */
@WebListener
public class ScheduleResource implements ServletContextListener {

    public static void TestCompents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ScheduledExecutorService scheduler;
    private ScheduledExecutorService schedulerForUT;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        long millisecondTimeToNextSunday = calculateTimeToSunday();

        scheduler.scheduleAtFixedRate(new SomeDailyJob(), millisecondTimeToNextSunday, 7, TimeUnit.DAYS);
        
        /*schedulerForUT = Executors.newSingleThreadScheduledExecutor();

        schedulerForUT.scheduleAtFixedRate(new UnitTestJob(), 0, 1, TimeUnit.DAYS);*/
    }

    private long calculateTimeToSunday() {
        Calendar calNow = Calendar.getInstance();

        Calendar calNextSat = Calendar.getInstance();
        calNextSat.set(Calendar.HOUR, 10);
        calNextSat.set(Calendar.MINUTE, 0);
        calNextSat.set(Calendar.SECOND, 0);
        calNextSat.set(Calendar.AM_PM, Calendar.AM);

        while (calNextSat.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calNextSat.add(Calendar.DATE, 1);
        }

        return calNextSat.getTime().getTime() - calNow.getTime().getTime();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

    private class UnitTestJob implements Runnable {

        @Override
        public void run() {
            UnitTestsResources resource = new UnitTestsResources();
            List<String> errors = new ArrayList<String>();
            resource.doUnitTests(errors);
            
            if (errors != null && !errors.isEmpty()) {
                sendReportToDevelopers(errors);
            }
        }
        
        private void sendReportToDevelopers(List<String> errors) {
            MailUtils.sendTo("Roomatched system", "Or Bar", "22orb22@gmail.com", "Roomatched alert system", errors.toString(), false);
            MailUtils.sendTo("Roomatched system", "Gil Dadosh", "gildadosh2@gmail.com", "Roomatched alert system", errors.toString(), false);
            MailUtils.sendTo("Roomatched system", "Boris Ablamunits‏ ", "ablamunits@gmail.com", "Roomatched alert system", errors.toString(), false);
        }
    }

    public class SomeDailyJob implements Runnable {

        @Override
        public void run() {
            IMatchesActions matchesActions = new MatchesActions();
            List users = getUsersWhoWantToReciveEmail();
            
            for (Object userObj : users) {
                Object[] objAsArray = (Object[]) userObj;
                User user = (User)objAsArray[0];
                UserSettings userSettings = (UserSettings)objAsArray[1];
                StringBuilder sb = new StringBuilder();
                
                List<JSONObject> currentUserMatches = matchesActions.getAllMatches(user.getId());
                if (!currentUserMatches.isEmpty()) {
                    sb.append(getStartOfHtml(user.getFirstName(), currentUserMatches.size()));
                    
                    int currIndexOfMatches = 0;

                    for (JSONObject json : currentUserMatches) {
                        if (currIndexOfMatches >= 3) {
                            break;
                        }
                        
                        MatchingUser matchingUser = getMatchingUserFromJson(json);
                        sb.append(getHtmlForMatchingUser(matchingUser));

                        if (user.getType().equalsIgnoreCase(configuration.Configuration.Types.seekerType)) {
                            RoomDetails roomDetails = getRoomDetailsFromJson(json);
                            ApartmentDetails apartment = getApartmentDetailsFromJson(json);

                            if (roomDetails != null && apartment != null) {
                                sb.append(getHtmlForRoomAndApartment(roomDetails, apartment));
                            }
                        }
                        
                        currIndexOfMatches++;
                    }
                    
                    sb.append(getEndOfHtml());
                    int numOfMatches = currentUserMatches.size();
                    String subject = user.getFirstName() + ", You got " + numOfMatches + (numOfMatches > 1? " matches!" : "match!");
                    MailUtils.sendTo("Roomatched Team", user.getFirstName(), userSettings.getEmail(), subject, sb.toString(), Boolean.TRUE);
                }
            }
        }

        private List<MatchingUser> convertJsonObjectMatchToPojoMatch(List<JSONObject> currentUserMatches) {
            List<MatchingUser> res = new ArrayList<>();

            for (JSONObject json : currentUserMatches) {
                // parse the json to matching user
                MatchingUser mu = getMatchingUserFromJson(json);
                res.add(mu);
            }

            return res;
        }

        private List getUsersWhoWantToReciveEmail() {
            List<User> users = new ArrayList<>();
            List relevantUsers = MySQLUtils.runQuery(QueriesUtils.getUsersWhoWantToReciveNotfiactions());

            return relevantUsers;
        }

        private MatchingUser getMatchingUserFromJson(JSONObject json) {
           MatchingUser res = new MatchingUser(null, 0, true, null);
           RoomDetails room = new RoomDetails();
           
            try {
                JSONObject userJson = json.getJSONObject("user");
                String firstName = userJson.getString("firstName");
                String lastName = userJson.getString("lastName");
                String UserPhotoUrl = userJson.getString("photoUrl");
                double matchPercentage = json.getDouble("matchPercentage");
                
                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhotoUrl(UserPhotoUrl);
                res.setUser(user);
                
                res.setPrecentage(matchPercentage);
            } catch (JSONException ex) {
                return null;
            } finally {
                return res;
            }
        }

        private RoomDetails getRoomDetailsFromJson(JSONObject json) {
            try {
                JSONObject roomJson = json.getJSONObject("room");

                RoomDetails room = new RoomDetails();
                room.setPhotoUrl(roomJson.optString("photoUrl"));
                room.setPrice(roomJson.getInt("price"));

                return room;
            } catch (Exception ex) {
                return null;
            }
        }

        private ApartmentDetails getApartmentDetailsFromJson(JSONObject json) {
            try {
                JSONObject apartmentJson = json.getJSONObject("apartment");

                ApartmentDetails apartment = new ApartmentDetails();
                apartment.setPhotoUrl(apartmentJson.optString("photoUrl"));
                apartment.setAddress(apartmentJson.getString("address"));
                apartment.setCity(apartmentJson.getString("city"));

                return apartment;
            } catch (Exception ex) {
                return null;
            }
        }
    }
    
    private String getStartOfHtml(String userName, int numOfMatches) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<!DOCTYPE html>\n" +
"<html lang=\"en\">\n" +
"    <head>\n" +
"	    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">\n" +
"		<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>\n" +
"    </head>\n" +
"    <body style=\"font-color: #EB5635; background-color: #f9f9f9\" \"margin: 0\" \"padding: 0\" \"font-family: sans-serif\">\n" +
"	<center>\n" +
"	    <div id=\"container\" style=\"color: #EB5635; border: 1px solid #EB5635; border-radius: 5px; font-color: #EB5635; background-color: #f9f9f9\" \"margin-bottom: 1em\" \"margin-left: auto\" \"margin-right: auto\" \"width: 90%\" \"height: auto\">\n" +
"		\n" +
"		<div class=\"header\" style=\"color: #EB5635; font-color: #EB5635; background-color: #f9f9f9\">\n" +
"			<h1 class = \"userName\"> Hello ");
        sb.append(userName);
        sb.append("</h1>\n" +
"		</div>\n" +
"		\n" +
"		<h1 style=\"font-color: #EB5635; padding: 1px\"> We have " + numOfMatches + " matches that may interest you</h1>");
        sb.append("<h1 style=\"font-color: #EB5635; padding: 5px\"> Here are the top scored matches!</h1>" + 
"		<table>\n");
        return sb.toString();
    }
    
    private String getEndOfHtml() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("		</table>\n" +
"			<br></br>\n" +
"		<br></br>\n" +
"<a style=\"font-size: 15px; display: block; margin: 10px 0; font-color: #EB5635;\" href=\"http://www.roomatched.com\">View on Roomatched</a>" +
"		</div>\n" +
"		<br></br>\n" +
"		</center>\n" +
"    </body>\n" +
"</html>");
        
        return sb.toString();
    }
    
    private String getHtmlForMatchingUser(MatchingUser matchingUser) {
        User user = matchingUser.getUser();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<tr></tr>");
        sb.append("<td><p id=\"User name\" style=\"font-size: 19px; font-color: #EB5635\"/>"+
                user.getFirstName() + " " + user.getLastName());
        sb.append("</td>\n" +
"				<td><p id=\"Matching precentage\" style=\"font-size: 19px; font-color: #EB5635\"/>");
        sb.append(new DecimalFormat("###.##").format(matchingUser.getPrecentage()) + "% Matching");
        sb.append("</td>\n");
        sb.append("<tr></tr>");
        if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
            sb.append("<td><img src=\"");
            sb.append(user.getPhotoUrl());
            sb.append("\" height=\"150\" width=\"150\" style=\"border:1px solid #021a40\"></td>");
        }
        
        return sb.toString();
    }
    
    private String getHtmlForRoomAndApartment(RoomDetails room, ApartmentDetails apartment) {
        StringBuilder sb = new StringBuilder();
        
        if (room.getPhotoUrl() != null && !room.getPhotoUrl().isEmpty()) {
            sb.append("<img src=\"");
            sb.append(room.getPhotoUrl());
            sb.append("\" height=\"150\" width=\"150\" style=\"border:1px solid #021a40\"/>\n" +
"		<br></br>\n");
        } else {
            sb.append("<td></td>");
        }
        
        sb.append("<td><p id=\"Address of the apartment\" style=\"font-size: 19px; font-color: #EB5635\"/>");
        sb.append(apartment.getAddress() + ", " + apartment.getCity());
        sb.append("</td><br></br>\n");
        sb.append("<td><p id=\"price\" style=\"font-size: 19px; font-color: #EB5635\"/>");
        sb.append(room.getPrice() + " NIS");
        sb.append("</td>\n" +
"			</tr>\n");
        return sb.toString();
    }
}
