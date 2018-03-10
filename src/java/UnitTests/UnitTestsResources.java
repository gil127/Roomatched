/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitTests;

import dao.ApartmentActions;
import dao.OffererPreferencesActions;
import dao.RoomActions;
import dao.PostsActions;
import dao.UserActions;
import dao.UserSettingsActions;
import dao.UserAboutActions;
import dao.interfaces.IPostActions;
import dao.interfaces.IPreferencesActions;
import dao.interfaces.IUserAboutActions;
import dao.interfaces.IUserActions;
import dao.interfaces.IUserSettingActions;
import dao.SeekerPreferencesActions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.User;
import pojos.UserSettings;
import pojos.UsersPreffSelection;
import pojos.interfaces.IUserPreferences;
import utils.JSONObjectUtils;
import utils.MySQLUtils;
import utils.QueriesUtils;
import utils.ResourceUtils;

/**
 *
 * @author Gil
 */
@Path("/unitTests")
public class UnitTestsResources {
    @Context
    HttpServletResponse response;
    
    private IUserActions userActions = new UserActions();

    @GET
    @Produces("application/json;charset=utf-8")
    public void testAllComponent() {
        List<String> report = new ArrayList<>();
        doUnitTests(report);

        if (report == null || report.isEmpty()) {
            report.add("passed all tests");
        }

        ResourceUtils.okResponse(response, report.toString());
    }

    private String readJsonFromFile(String file) throws IOException {
        StringBuilder sb = new StringBuilder();
        File f = new File(file);    
        if (f == null || !f.exists()) {
            throw new IOException("The file " + f.getAbsolutePath()+ " is not exist");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine);
            }

        } catch (IOException e) {
            throw e;
        } finally {
            return sb.toString();
        }
    }

    private String getUserToTestFbId(JSONObject userToTestJson) throws JSONException {
        return userToTestJson.getString("facebookId");
    }

    private void deleteSeekerUserAtEndOfTheTest(int id, String facebookId) {
        String query = QueriesUtils.createQueryForDeleteSeekerPref(id);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeleteUserSettings(id);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeleteUser(id);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeleteFriendship(facebookId);
        MySQLUtils.runDeleteQuery(query);
    }

    private void deleteOffererUserAtEndOfTheTest(int offererId, String facebookId, int roomId, int apartmentId) {
        String query = QueriesUtils.createQueryForDeleteOffererPref(offererId);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeleteUserSettings(offererId);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeleteFriendship(facebookId);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeletePost(offererId);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeleteRoomDetails(roomId);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeleteApartmentDetails(apartmentId);
        MySQLUtils.runDeleteQuery(query);
        query = QueriesUtils.createQueryForDeleteUser(offererId);
        MySQLUtils.runDeleteQuery(query);
    }

    private void testOffererUser(List<String> errors) {
        User userToTest = null;
        int apartmentId = 0, roomId = 0;
        try {
            JSONObject userToTestJson = ExampleJsons.getOffererUserForCreation();

            List<String> createUserErrors;
            try {
                createUserErrors = userActions.createUser(userToTestJson);
            } catch (Exception ex) {
                createUserErrors = new ArrayList<String>();
                createUserErrors.add(ex.getMessage());
            }
            
            if (createUserErrors != null && !createUserErrors.isEmpty()) {
                errors.addAll(createUserErrors);
            } else {
                String fbId = getUserToTestFbId(userToTestJson);
                userToTest = userActions.findUserByFacebookId(fbId);
                JSONObject postJson = getRoomFromOffererId(userToTest.getId());
                apartmentId = getApartmentIdFromPostJson(postJson);
                roomId = getRoomIdFromPostJson(postJson);
                
                testOffererUserPrff(errors, userToTest.getId());
                testUserSettings(errors, userToTest.getId());
                testUserAbout(errors, userToTest.getId());
                testRoomDetails(errors, roomId);
                testApartmentDetails(errors, apartmentId);
            }
        } catch (Exception ex) {
            errors.add(ex.getMessage());
        } finally {
            if (userToTest != null) {
                deleteOffererUserAtEndOfTheTest(userToTest.getId(), userToTest.getFacebookId(), roomId, apartmentId);
            }
        }
    }

    private void testSeekerUser(List<String> errors) {
        User userToTest = null;
        try {
            JSONObject userToTestJson = ExampleJsons.getSeekerUserForCreation();

            List<String> createUserErrors;
            try {
                createUserErrors = userActions.createUser(userToTestJson);
            } catch (Exception ex) {
                createUserErrors = new ArrayList<String>();
                createUserErrors.add(ex.getMessage());
            }
            
            if (createUserErrors != null && !createUserErrors.isEmpty()) {
                errors.addAll(createUserErrors);
            } else {
                String fbId = getUserToTestFbId(userToTestJson);
                userToTest = userActions.findUserByFacebookId(fbId);
                
                testSeekerUserPreff(errors, userToTest.getId());
                testUserSettings(errors, userToTest.getId());
                testUserAbout(errors, userToTest.getId());
            }
        } catch (Exception ex) {
            errors.add(ex.getMessage());
        } finally {
            if (userToTest != null) {
                deleteSeekerUserAtEndOfTheTest(userToTest.getId(), userToTest.getFacebookId());
            }
        }
    }

    private void testOffererUserPrff(List<String> errors, int id) throws JSONException, IOException {
        IPreferencesActions userToTestPrefrences = new OffererPreferencesActions();

        IUserPreferences offererPreffToUpdatePreff = JSONObjectUtils.createOffererPreferencesFromJsonObject(ExampleJsons.getOffererUserPreff());
        String updatePreffErrors;
        try {
            updatePreffErrors = userToTestPrefrences.updateUserPreferences(id, offererPreffToUpdatePreff);
            updatePreffSelection(offererPreffToUpdatePreff);
        } catch (Exception ex) {
            updatePreffErrors = ex.getMessage();
        }
        
        if (updatePreffErrors != null && !updatePreffErrors.isEmpty()) {
            errors.add(updatePreffErrors);
        }
    }

    private void testSeekerUserPreff(List<String> errors, int id) throws JSONException, IOException {
        IPreferencesActions userToTestPrefrences = new SeekerPreferencesActions();

        JSONObject userToTestPreffJson = ExampleJsons.getUpdateSeekerUser();
        IUserPreferences seekerPreffToUpdatePreff = JSONObjectUtils.createSeekerPreferencesFromJsonObject(userToTestPreffJson);
        String updatePreffErrors;
        try {
            updatePreffErrors = userToTestPrefrences.updateUserPreferences(id, seekerPreffToUpdatePreff);
            updatePreffSelection(seekerPreffToUpdatePreff);
        } catch (Exception ex) {
            updatePreffErrors = ex.getMessage();
        }
        
        if (updatePreffErrors != null && !updatePreffErrors.isEmpty()) {
            errors.add(updatePreffErrors);
        }
    }

    private void testUserSettings(List<String> errors, int id) throws JSONException, IOException {
        IUserSettingActions userToTestSettings = new UserSettingsActions();
        JSONObject userSettingsToTestJson = ExampleJsons.getUserSettings();
        UserSettings userSettinsToUpdate = JSONObjectUtils.getUserSettingsFromJsonObject(userSettingsToTestJson);

        List<String> updateUserSettingsErrors;
        try {
            updateUserSettingsErrors = userToTestSettings.updateUserSettings(id, userSettinsToUpdate);
        } catch (Exception ex) {
            updateUserSettingsErrors = new ArrayList<String>();
            updateUserSettingsErrors.add(ex.getMessage());
        }
        
        if (updateUserSettingsErrors != null && !updateUserSettingsErrors.isEmpty()) {
            errors.addAll(updateUserSettingsErrors);
        }
    }

    private void testUserAbout(List<String> errors, int id) throws IOException {
        IUserAboutActions userToTestAbout = new UserAboutActions();
        JSONObject userAbout = userToTestAbout.getUserAbout(id);

        if (userAbout.length() != 0) { // make sure that the test file has value in about field
            errors.add("error occurred while trying to read user about");
        }

        String userSettingsToUpdateStr = ExampleJsons.getUserAbout();
        boolean updateUserAboutFlag = userToTestAbout.updateUserAbout(id, userSettingsToUpdateStr);

        if (!updateUserAboutFlag) {
            errors.add("error occurred while trying to update user about");
        }
    }

    public void doUnitTests(List<String> report) {
        testSeekerUser(report);
        testOffererUser(report);
    }

    private void updatePreffSelection(IUserPreferences seekerPreffToUpdatePreff) throws Exception {
        IPreferencesActions userToTestPrefrences = new SeekerPreferencesActions();
        
        List<UsersPreffSelection> userPreffSelectionsFromDB = userToTestPrefrences.getUserPreffSelectionsFromDB();
        userToTestPrefrences.updateUserSelectionAfterChange(seekerPreffToUpdatePreff, userPreffSelectionsFromDB);
        EntityManager em = MySQLUtils.getEntityManager();
        MySQLUtils.beginTransaction(em);

        for (UsersPreffSelection userPreffSelection : userPreffSelectionsFromDB) {
            MySQLUtils.Update(userPreffSelection, em);
        }

        MySQLUtils.commitTransaction(em);
    }

    private void testRoomDetails(List<String> errors, int roomId) throws JSONException {
        try {
            RoomActions roomActions = new RoomActions();
            List<String> updateRoomErrors = roomActions.updateRoomDetails(roomId, JSONObjectUtils.createRoomFromJsonObject(ExampleJsons.getRoomDetails()));

            if (updateRoomErrors != null && !updateRoomErrors.isEmpty()) {
                errors.addAll(errors);
            }
        } catch (Exception ex) {
            errors.add(ex.getMessage());
        }
    }

    private void testApartmentDetails(List<String> errors, int apartmentId) {
        try {
            ApartmentActions apartmentActions = new ApartmentActions();
            List<String> updateApartmentErrors = apartmentActions.updateApartmentDetails(apartmentId, JSONObjectUtils.createApartmentFromJson(ExampleJsons.getApartmentDetails()));

            if (updateApartmentErrors != null && !updateApartmentErrors.isEmpty()) {
                errors.addAll(errors);
            }
        } catch (Exception ex) {
            errors.add(ex.getMessage());
        }
    }

    private JSONObject getRoomFromOffererId(int offererId) {
        IPostActions postActions = new PostsActions();
        return postActions.getJsonOfPostByOffererId(offererId);
    }
    
    private int getApartmentIdFromPostJson(JSONObject postJson) throws JSONException {
        JSONObject apartmentJson = postJson.getJSONObject("apartment");
        return apartmentJson.getInt("apartmentId");
    }
    
    private int getRoomIdFromPostJson(JSONObject postJson) throws JSONException {
        JSONObject roomJson = postJson.getJSONObject("room");
        return roomJson.getInt("roomId");
    }
}
