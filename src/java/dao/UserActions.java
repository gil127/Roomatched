package dao;

import dao.interfaces.IUserActions;
import org.json.JSONObject;
import utils.MySQLUtils;
import configuration.Configuration;
import dao.interfaces.IFriendshipActions;
import pojos.ApartmentDetails;
import pojos.Post;
import pojos.interfaces.IUserPreferences;
import dao.interfaces.IPreferencesActions;
import pojos.RoomDetails;
import pojos.UserSettings;
import pojos.User;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.json.JSONArray;
import org.json.JSONException;
import pojos.Friendship;
import pojos.UsersPreffSelection;
import utils.JSONObjectUtils;
import utils.MailUtils;

public class UserActions implements IUserActions{

    @Override
    public List<User> getAllUsers() {
        List<User> users = (List<User>)(List<?>) MySQLUtils.retrieveAll(User.class);
        System.out.println(users);
        if (users.isEmpty()) {
            return null;
        } else {
            return users;
        } 
    }

    @Override
    public User findUserById(int id) {
        User user = (User) MySQLUtils.retrieveById(User.class ,id);
        if (user != null) {
            return user;
        } else {
            return null;
        } 
    }
    
    @Override
    public User findUserByFacebookId(String fbId) {
        User user = (User) MySQLUtils.retrieveUserByFacebookId(User.class ,fbId);
        if (user != null) {
            return user;
        } else {
            return null;
        } 
    }
 
    @Override
    public List<String> createUser(JSONObject jason) throws Exception{
        User user = JSONObjectUtils.getUserFromJsonObject(jason);
        List<Friendship> facebookFriends = getFacebookFriendListFromJsonObject(jason);
        if (user == null) {
            List<String> errors = new ArrayList<String>(); 
            errors.add("There are fields missing in user json format");
            return errors;
        }
        
        List<String> errors = checkIfUserDetailsAreValid(user);
        if (errors.isEmpty()) {
            IPreferencesActions prefActions;
            if (user.getType().equalsIgnoreCase(Configuration.Types.seekerType)) {
                prefActions = new SeekerPreferencesActions();
            } else {
                prefActions = new OffererPreferencesActions();
            }
            
            IUserPreferences userPref = prefActions.createConcretePreferencesFromJsonObject(jason);
            
            String userPrefError = prefActions.checkValidation(userPref);
            
            if (userPref != null && userPrefError == null) {
                List<UsersPreffSelection> userSelection = prefActions.createUserSelectionByUserPref(userPref);

                if (userSelection == null) {
                    errors.add("There is a problem in userPreffSelection table");
                }
                ApartmentDetails apartment = null;
                RoomDetails room = null;
                if (user.getType().equalsIgnoreCase(Configuration.Types.offererType)) {
                    apartment = JSONObjectUtils.createApartmentFromJson(jason);
                    List<String> apartmentErrors = ApartmentActions.checkValidation(apartment);
                    
                    if (apartmentErrors.isEmpty()) {
                        room = JSONObjectUtils.createRoomFromJsonObject(jason);
                        List<String> roomErrors = RoomActions.checkValidation(room);
                        if (!roomErrors.isEmpty()) {
                            errors.addAll(roomErrors);
                        }
                    } else {
                        errors.addAll(apartmentErrors);
                    }
                }
                
                if (errors.isEmpty()) {
                    UserSettings userSettings = UserSettingsActions.createDefaultSettings();
                    
                    String insertToDBError = insertIntoDB(user, userPref, userSelection, apartment, room, userSettings, facebookFriends);
                    
                    if (insertToDBError != null) {
                        errors.add(insertToDBError);
                    }
                }
            } else {
                errors.add(userPrefError);
            }
        } 
        
        return errors;
    }

    @Override
    public boolean deleteUserById(int id) {
        User user = (User) MySQLUtils.retrieveById(User.class, id);
        if (user != null) {
            MySQLUtils.delete(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> updateUser(int id, User user) {
        List<String> errors = checkIfUserDetailsAreValid(user);
        
        if (errors != null && !errors.isEmpty()) {
            return errors;
        }
        
        User userFromDB = (User) MySQLUtils.retrieveById(User.class, id);
        if (userFromDB != null) {
            user.setId(userFromDB.getId());
            MySQLUtils.Update(user);
            return null;
        } else {
            errors = new ArrayList<String>();
            errors.add("There is no user with userId " + id);
            return errors;
        }
    }

    public List<String> checkIfUserDetailsAreValid(User user) {
        List<String> errorDescripiton = new ArrayList<String>();
        
        if (user == null) {
            errorDescripiton.add("There are invalid keys in the Jason object");
        } else {
            String email = user.getEmail();
            String password = user.getPassword();
            String type = user.getType();

            if(email != null) {
                errorDescripiton.addAll(MailUtils.checkEmailValidation(email));   
            }
            
            if (password != null) {
                errorDescripiton.addAll(checkValidityOfPassword(password));   
            }
            
            errorDescripiton.addAll(checkValidityOfType(type));
        }

        return errorDescripiton;
    }

    private List<String> checkValidityOfPassword(String password) {
        List<String> errors = new ArrayList<String>();
        
        if (!(password.length() > 3 && password.length() < 13)) {
            errors.add("Your password length should be between 4-12 character");
        }
        if (passwordContainsLettersAndDigit(password)) {
            errors.add("Your password should contain both letters and digits");
        }
        return errors;
    }

    private boolean passwordContainsLettersAndDigit(String password) {
        boolean containLetter = false, containDigit = false;

        for (Character ch : password.toCharArray()) {
            if (Character.isLetter(ch)) {
                containLetter = true;
            } else if (Character.isDigit(ch)) {
                containDigit = true;
            }
            
            if (containDigit && containLetter) {
                break;
            }
        }
        
        return containLetter && containDigit;
    }

    private List<String> checkNameValidation(String firstName, String lastName) {
        List<String> errors = new ArrayList<String>();
        
        if (firstName == null) {
            errors.add("Your first name is empty");
        }
        
        if (!isStringContainsOnlyChars(firstName)) {
            errors.add("Your first name contains invalid characters");
        }
        
        if (lastName == null) {
            errors.add("Your last name is empty");
        }
        
        if (!isStringContainsOnlyChars(lastName)) {
            errors.add("Your last name contains invalid characters");
        }
        
        return errors;
    }
    
    private boolean isStringContainsOnlyChars(String stringToCheck) {
        for (Character c : stringToCheck.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        
        return true;
    }

    private List<String> checkValidityOfType(String type) {
        List<String> errors = new ArrayList<String>();
        
        if (!type.equalsIgnoreCase(Configuration.Types.offererType) && !type.equalsIgnoreCase(Configuration.Types.seekerType)) {
            errors.add("The type is not suitable");
        }
        return errors;
    }

    private String insertIntoDB(User user, IUserPreferences userPref, List<UsersPreffSelection> userSelction, ApartmentDetails apartment, RoomDetails room, UserSettings userSettings, List<Friendship> facebookFriends) throws Exception {
        EntityManager em = MySQLUtils.getEntityManager();
        MySQLUtils.beginTransaction(em);
        user = (User) MySQLUtils.persist(user, em);
        userPref.setUserId(user.getId());
        MySQLUtils.persist(userPref, em);
        
        insertToDBUsersSelectionPreff(userSelction, em);

        userSettings.setUserId(user.getId());
        MySQLUtils.persist(userSettings, em);
        insertToDBFacebookFriends(em, facebookFriends);
        
        if (apartment != null && room != null) {
            apartment.setOffererUserId(user.getId());
            apartment = (ApartmentDetails) MySQLUtils.persist(apartment, em);
            room.setApartmentId(apartment.getApartmentId());
            room = (RoomDetails) MySQLUtils.persist(room, em);

            Post post = new Post();
            post.setOffererId(user.getId());
            post.setRoomId(room.getId());
            MySQLUtils.persist(post, em);
        }
        
        return MySQLUtils.commitTransaction(em);
    }

    private List<String> checkValidityOfSex(String type) {
        List<String> errors = new ArrayList<String>();
        if (!type.equalsIgnoreCase(Configuration.Gender.MALE) && !type.equalsIgnoreCase(Configuration.Gender.FEMALE)) {
            errors.add("type is not suitable");
        }
        return errors;
    }

    private List<String> checkValidtyOfPhotoUrl(String photos) {
        List<String> errors = new ArrayList<String>();
        try {
            if (!photos.isEmpty()) {
                URL url = new URL(photos);
                URLConnection conn = url.openConnection();
                conn.connect();
            }
        } catch (MalformedURLException e) {
            // the URL is not in a valid form
            errors.add(e.getMessage());
        } catch (IOException e) {
            // the connection couldn't be established
            errors.add(e.getMessage());
        }
        
        return errors;
    }

    private List<Friendship> getFacebookFriendListFromJsonObject(JSONObject jason) {
        List<Friendship> fbFriends = new ArrayList<>();
        
        try {
            String userFacebookId = jason.getString("facebookId");
            JSONArray facebookIds = jason.optJSONArray("facebookFriends");
            
            IFriendshipActions friendshipActions = new FriendshipActions();
            List<Friendship> userFriendsList = friendshipActions.getUserFriendsList(userFacebookId);
                        
            for (int i = 0; i < facebookIds.length(); i++) {
                String fbFrString = facebookIds.getString(i);
                if (checkIfFriendshipIsNotInTheList(fbFrString, userFacebookId, userFriendsList)) {
                    Friendship friendship = new Friendship();
                    friendship.setUserOnefacebookID(userFacebookId);
                    friendship.setUserTwoFacebookID(fbFrString);
                    fbFriends.add(friendship);
                }
            }
        } catch (Exception ex) {
            String msg = ex.getMessage();
        } finally {
            return fbFriends;
        }
    }

    private void insertToDBFacebookFriends(EntityManager em, List<Friendship> facebookFriends) throws Exception {
        for (Friendship facebookFriend : facebookFriends) {
            MySQLUtils.persist(facebookFriend, em);
        }
    }

    private boolean checkIfFriendshipIsNotInTheList(String fbFrString, String userFacebookId, List<Friendship> userFriendsList) {
        for (Friendship friendship : userFriendsList) {
            String userOne = friendship.getUserOnefacebookID();
            String userTwo = friendship.getUserTwofacebookID();
            if ((userOne.equalsIgnoreCase(userFacebookId) && userTwo.equalsIgnoreCase(fbFrString))
               || (userOne.equalsIgnoreCase(fbFrString) && userTwo.equalsIgnoreCase(userFacebookId))) {
                return false;
            }
        }
        
        return true;
    }

    

    private void insertToDBUsersSelectionPreff(List<UsersPreffSelection> userSelction, EntityManager em) {
        for (UsersPreffSelection userPreffSelection : userSelction){
            MySQLUtils.Update(userPreffSelection, em);
        }
    }
}