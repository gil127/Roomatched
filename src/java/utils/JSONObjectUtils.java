package utils;

import configuration.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.ApartmentDetails;
import pojos.MatchingUser;
import pojos.OffererPreferences;
import pojos.SeekerPreferences;
import pojos.RoomDetails;
import pojos.User;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.Conversation;
import pojos.MessageClient;
import pojos.UserSettings;
import pojos.interfaces.IUserPreferences;

public class JSONObjectUtils {
    public static User getUserFromJsonObject(JSONObject jason) {
        try {
            User user = new User();
            user.setFirstName(jason.getString("firstName"));
            user.setFacebookId(jason.getString("facebookId"));
            user.setLastName(jason.getString("lastName"));
            user.setSex(jason.getString("sex"));
            user.setType(jason.getString("type"));
            user.setPhotoUrl(jason.optString("photoUrl", null));
            user.setPassword(jason.optString("password", null));
            user.setEmail(jason.optString("email", null));
            user.setYearOfBirth(jason.optInt("yearOfBirth", Configuration.NO_VALUE));
            user.setAbout(jason.optString("about", null));
            
            return user;
        } catch (JSONException ex) {
            return null;
        }
    }
    
    public static RoomDetails createRoomFromJsonObject(JSONObject json) {
        try {
            RoomDetails room = new RoomDetails();
            JSONObject additionalDetails = json.optJSONObject("additionalDetails");
            JSONObject roomJson = additionalDetails.optJSONObject("roomDetails");
            
            room.setAirConditioned(roomJson.optBoolean("airConditioned", false));
            room.setSeperateBathroom(roomJson.optBoolean("seperateBathroom", false));
            room.setHasBalcony(roomJson.optBoolean("hasBalcony", false));
            room.setIsFurnished(roomJson.optBoolean("isFurnished", false));
            room.setSize(roomJson.optInt("size", 0));
            room.setPrice(roomJson.getInt("price"));
            room.setPhotoUrl(roomJson.optString("photoUrl", null));
            
            return room;
        } catch (JSONException ex) {
            return null;
        }
    }
    
    public static ApartmentDetails createApartmentFromJson(JSONObject json) {
        try {
            ApartmentDetails apartment = new ApartmentDetails();
            JSONObject additionalDetails = json.optJSONObject("additionalDetails");
            JSONObject jsonApartment = additionalDetails.optJSONObject("apartmentDetails");
            
            apartment.setAddress(jsonApartment.getString("address"));
            apartment.setSize(jsonApartment.optInt("size", 0));
            apartment.setTotalRooms(jsonApartment.optInt("totalRooms", 0));
            apartment.setTotalRoomates(jsonApartment.optInt("totalRoomates", 0));
            apartment.setFreeRooms(jsonApartment.optInt("freeRooms", 1));
            apartment.setFloor(jsonApartment.optInt("floor", 0));
            apartment.setHasElevator(jsonApartment.optBoolean("hasElevator", false));
            apartment.setHasParking(jsonApartment.optBoolean("hasParking", false));
            apartment.setIsRenovated(jsonApartment.optBoolean("isRenovated", false));
            apartment.setApproximateExpences(jsonApartment.optInt("approximateExpences", 0));
            apartment.setGuarantees(jsonApartment.optString("guarantees", null));
            apartment.setHasLivingRoom(jsonApartment.optBoolean("hasLivingRoom", false));
            apartment.setCity(jsonApartment.getString("city"));
            apartment.setIsFurnished(jsonApartment.optBoolean("isFurnished", false));
            apartment.setPhotoUrl(jsonApartment.optString("photoUrl", null));
            
            return apartment;
        } catch (JSONException ex) {
            return null;
        }
    }
    
    public static JSONObject getPostJsonObjectFromPojos(int postId, User user, RoomDetails room, ApartmentDetails apartment) {
        try {
            JSONObject userJson = new JSONObject();
            
            userJson.put("userId", user.getId());
            userJson.put("firstName", user.getFirstName());
            userJson.put("lastName", user.getLastName());
            userJson.put("sex", user.getSex());
            userJson.put("photoUrl", user.getPhotoUrl());
            userJson.put("yearOfBirth", user.getYearOfBirth() == Configuration.NO_VALUE? null : user.getYearOfBirth());
            
            JSONObject roomJson = new JSONObject();
            
            roomJson.put("roomId", room.getId());
            roomJson.put("size", room.getSize());
            roomJson.put("price", room.getPrice());
            roomJson.put("photoUrl", room.getPhotoUrl());
            roomJson.put("hasBalcony", room.getHasBalcony());
            roomJson.put("airConditioned", room.getAirConditioned());
            roomJson.put("seperateBathroom", room.getSeperateBathroom());
            roomJson.put("isFurnished", room.getIsFurnished());
            roomJson.put("apartmentId", room.getApartmentId());
            
            JSONObject apartmentJson = new JSONObject();
            
            apartmentJson.put("apartmentId", apartment.getApartmentId());
            apartmentJson.put("address", apartment.getAddress());
            apartmentJson.put("city", apartment.getCity());
            apartmentJson.put("floor", apartment.getFloor());
            apartmentJson.put("freeRooms", apartment.getFreeRooms());
            apartmentJson.put("totalRoomates", apartment.getTotalRoomates());
            apartmentJson.put("totalRooms", apartment.getTotalRooms());
            apartmentJson.put("offererUserId", apartment.getOffererUserId());
            apartmentJson.put("size", apartment.getSize());
            apartmentJson.put("approximateExpences", apartment.getApproximateExpences());
            apartmentJson.put("photoUrl", apartment.getPhotoUrl());
            
            apartmentJson.put("hasLivingRoom", apartment.getHasLivingRoom());
            apartmentJson.put("hasElevator", apartment.getHasElevator());
            apartmentJson.put("isRenovated", apartment.getIsRenovated());
            apartmentJson.put("hasParking", apartment.getHasParking());
            apartmentJson.put("isFurnished", apartment.getIsFurnished());
           
            JSONObject json = new JSONObject();
            
            json.put("postId", postId); // Add postId for every post.
            json.put("user", userJson);
            json.put("room", roomJson);
            json.put("apartment", apartmentJson);
            
            return json;
        } catch (JSONException ex) {
            return null;
        }
    }
        
    public static JSONObject getSeekerMatchJsonObjectFromPojos(int postId, MatchingUser matchingUser, 
            OffererPreferences offererPref, RoomDetails room, ApartmentDetails apartment, 
            Boolean isFavorite, Boolean hasConv, Boolean isPerfectFavorite) {
        try {
            JSONObject userJson = new JSONObject();
            
            User user = matchingUser.getUser();
            
            userJson.put("id", user.getId());
            userJson.put("firstName", user.getFirstName());
            userJson.put("lastName", user.getLastName());
            userJson.put("sex", user.getSex());
            userJson.put("about", user.getAbout());
            userJson.put("photoUrl", user.getPhotoUrl());
            userJson.put("yearOfBirth", user.getYearOfBirth() == Configuration.NO_VALUE? null : user.getYearOfBirth());
            
            
            JSONObject roomJson = new JSONObject();
            
            roomJson.put("roomId", room.getId());
            roomJson.put("size", room.getSize());
            roomJson.put("price", room.getPrice());
            roomJson.put("photoUrl", room.getPhotoUrl());
            roomJson.put("hasBalcony", room.getHasBalcony());
            roomJson.put("airConditioned", room.getAirConditioned());
            roomJson.put("seperateBathroom", room.getSeperateBathroom());
            roomJson.put("isFurnished", room.getIsFurnished());
            roomJson.put("apartmentId", room.getApartmentId());
            
            JSONObject apartmentJson = new JSONObject();
            
            apartmentJson.put("apartmentId", apartment.getApartmentId());
            apartmentJson.put("address", apartment.getAddress());
            apartmentJson.put("city", apartment.getCity());
            apartmentJson.put("floor", apartment.getFloor());
            apartmentJson.put("freeRooms", apartment.getFreeRooms());
            apartmentJson.put("totalRoomates", apartment.getTotalRoomates());
            apartmentJson.put("totalRooms", apartment.getTotalRooms());
            apartmentJson.put("offererUserId", apartment.getOffererUserId());
            apartmentJson.put("size", apartment.getSize());
            apartmentJson.put("approximateExpences", apartment.getApproximateExpences());
            apartmentJson.put("photoUrl", apartment.getPhotoUrl());
            
            apartmentJson.put("hasLivingRoom", apartment.getHasLivingRoom());
            apartmentJson.put("hasElevator", apartment.getHasElevator());
            apartmentJson.put("isRenovated", apartment.getIsRenovated());
            apartmentJson.put("hasParking", apartment.getHasParking());
            apartmentJson.put("isFurnished", apartment.getIsFurnished());
           
            JSONObject prefJson = new JSONObject();
            
            prefJson.put("animals", offererPref.getAnimals());
            prefJson.put("kosher", offererPref.getKosher());
            prefJson.put("gayFriendly", offererPref.getGayFriendly());
            prefJson.put("musicianFriendly", offererPref.getMusicianFriendly());
            prefJson.put("sharedExpences", offererPref.getSharedExpences());
            prefJson.put("smoking", offererPref.getSmoking());
            prefJson.put("vegan", offererPref.getVegan());
            prefJson.put("nightLife", offererPref.getNightLife());
            prefJson.put("firstValuablePref", offererPref.getFirstValuablePref());
            prefJson.put("secondValuablePref", offererPref.getSecondValuablePref());
            prefJson.put("thirdValuablePref", offererPref.getThirdValuablePref());
            
            
            JSONObject json = new JSONObject();
            
            json.put("postId", postId); // Add postId for every post.
            json.put("matchPercentage", matchingUser.getPrecentage());
            json.put("isFacebookFriend", matchingUser.isFacebookFriend());
            json.put("mutualFriends", matchingUser.getListOfMutualFriends().toArray());
            json.put("user", userJson);
            json.put("room", roomJson);
            json.put("apartment", apartmentJson);
            json.put("preferences", prefJson);
            json.put("isFavorite", isFavorite);
            json.put("hasConversation", hasConv);
            json.put("perfectFavourite", isPerfectFavorite);
            
            return json;
        } catch (JSONException ex) {
            return null;
        }
    }
    
    public static JSONObject getOffererMatchJsonObjectFromPojos(MatchingUser matchingUser, SeekerPreferences seekerPref, 
            Boolean isFavorite, Boolean hasConv, Boolean isPerfectFavorite) {
        try {
            JSONObject userJson = new JSONObject();
            
            User user = matchingUser.getUser();
            
            userJson.put("id", user.getId());
            userJson.put("about", user.getAbout());
            userJson.put("firstName", user.getFirstName());
            userJson.put("lastName", user.getLastName());
            userJson.put("sex", user.getSex());
            userJson.put("photoUrl", user.getPhotoUrl());
            userJson.put("yearOfBirth", user.getYearOfBirth() == Configuration.NO_VALUE? null : user.getYearOfBirth());
            
            JSONObject prefJson = new JSONObject();
            
            prefJson.put("animals", seekerPref.getAnimals());
            prefJson.put("kosher", seekerPref.getKosher());
            prefJson.put("gayFriendly", seekerPref.getGayFriendly());
            prefJson.put("musicianFriendly", seekerPref.getMusicianFriendly());
            prefJson.put("sharedExpences", seekerPref.getSharedExpences());
            prefJson.put("smoking", seekerPref.getSmoking());
            prefJson.put("vegan", seekerPref.getVegan());
            prefJson.put("nightLife", seekerPref.getNightLife());
            prefJson.put("firstValuablePref", seekerPref.getFirstValuablePref());
            prefJson.put("secondValuablePref", seekerPref.getSecondValuablePref());
            prefJson.put("thirdValuablePref", seekerPref.getThirdValuablePref());
            
            JSONObject json = new JSONObject();
            
            json.put("matchPercentage", matchingUser.getPrecentage());
            json.put("isFacebookFriend", matchingUser.isFacebookFriend());
            json.put("mutualFriends", matchingUser.getListOfMutualFriends().toArray());
            json.put("user", userJson);
            json.put("preferences", prefJson);
            json.put("isFavorite", isFavorite);
            json.put("hasConversation", hasConv);
            json.put("perfectFavourite", isPerfectFavorite);
            
            return json;
        } catch (JSONException ex) {
            return null;
        }
    }

    public static JSONObject getJsonObjectFromConv(Conversation conv, User user) {
        try {
            JSONObject json = new JSONObject();
            
            json.put("conversationId", conv.getConversationId());
            json.put("userId", user.getId());
            json.put("firstName", user.getFirstName());
            json.put("lastName", user.getLastName());
            json.put("photoUrl", user.getPhotoUrl());

            return json;
        } catch (JSONException ex) {
            return null;
        }
    }

    public static JSONObject getJsonObjectFromMsg(MessageClient msg) {
        try {
            JSONObject json = new JSONObject();
            
            json.put("from", msg.getFrom());
            json.put("to", msg.getTo());
            json.put("content", msg.getContent());
            json.put("messageId", msg.getMessageId());
            json.put("timeStamp", msg.getTimeStamp());
            
            return json;
        } catch (JSONException ex) {
            return null;
        }
    }
    
    public static IUserPreferences createOffererPreferencesFromJsonObject(JSONObject jason) {
        try {
            OffererPreferences offerer = new OffererPreferences();
            JSONObject prefJson = jason.optJSONObject("preferences");
            
            offerer.setAnimals(prefJson.getInt("animals"));
            offerer.setKosher(prefJson.getInt("kosher"));
            offerer.setSharedExpences(prefJson.getInt("sharedExpences"));
            offerer.setSmoking(prefJson.getInt("smoking"));
            offerer.setVegan(prefJson.getInt("vegan"));
            offerer.setGayFriendly(prefJson.getInt("gayFriendly"));
            offerer.setMusicianFriendly(prefJson.getInt("musicianFriendly"));
            offerer.setNightLife(prefJson.getInt("nightLife"));
            offerer.setSeekerOccupation(prefJson.optString("seekerOccupation", null));
            
            JSONObject additionalDetails = jason.optJSONObject("additionalDetails");
            if (additionalDetails == null) {
                offerer.setSexPreffered(configuration.Configuration.Gender.ANY);
            } else {
                offerer.setSexPreffered(additionalDetails.getString("sexPreffered"));
            }
            
            JSONObject mvp = jason.optJSONObject("mostValuablePreferences");
            if (mvp != null) {
                List<String> mvpList = new ArrayList<String>();
                
                if (mvp.getBoolean("animals")) {
                    mvpList.add("animals");
                }
                
                if (mvp.getBoolean("kosher")) {
                    mvpList.add("kosher");
                }
                
                if (mvp.getBoolean("sharedExpences")) {
                    mvpList.add("sharedExpences");
                }
                
                if (mvp.getBoolean("smoking")) {
                    mvpList.add("smoking");
                }
                
                if (mvp.getBoolean("vegan")) {
                    mvpList.add("vegan");
                }
                
                if (mvp.getBoolean("gayFriendly")) {
                    mvpList.add("gayFriendly");
                }
                
                if (mvp.getBoolean("musicianFriendly")) {
                    mvpList.add("musicianFriendly");
                }
                
                if (mvp.getBoolean("nightLife")) {
                    mvpList.add("nightLife");
                }
                
                offerer.setFirstValuablePref(mvpList.size() > 0? mvpList.get(0): null);
                offerer.setSecondValuablePref(mvpList.size() > 1? mvpList.get(1): null);
                offerer.setThirdValuablePref(mvpList.size() > 2? mvpList.get(2): null);
            }
            
            return offerer;
        } catch (JSONException ex) {
            return null;
        }
    }

    public static JSONObject createJsonObjectFromOffererPreferences(OffererPreferences userPref) {
        try {
            JSONObject json = new JSONObject();
            
            JSONObject prefJson = new JSONObject();
            
            prefJson.put("animals", userPref.getAnimals());
            prefJson.put("kosher", userPref.getKosher());
            prefJson.put("sharedExpences", userPref.getSharedExpences());
            prefJson.put("smoking", userPref.getSmoking());
            prefJson.put("vegan", userPref.getVegan());
            prefJson.put("gayFriendly", userPref.getGayFriendly());
            prefJson.put("musicianFriendly", userPref.getMusicianFriendly());
            prefJson.put("nightLife", userPref.getNightLife());
            prefJson.put("seekerOccupation", userPref.getSeekerOccupation());
            prefJson.put("sexPreffered", userPref.getSexPreffered());
            
            JSONObject mvp = new JSONObject();
            mvp.put("animals", checkIfPrefIsMVP("animals", userPref));
            mvp.put("kosher", checkIfPrefIsMVP("kosher", userPref));
            mvp.put("sharedExpences", checkIfPrefIsMVP("sharedExpences", userPref));
            mvp.put("smoking", checkIfPrefIsMVP("smoking", userPref));
            mvp.put("vegan", checkIfPrefIsMVP("vegan", userPref));
            mvp.put("gayFriendly", checkIfPrefIsMVP("gayFriendly", userPref));
            mvp.put("musicianFriendly", checkIfPrefIsMVP("musicianFriendly", userPref));
            mvp.put("nightLife", checkIfPrefIsMVP("nightLife", userPref));
            
            json.put("mostValuablePreferences", mvp);
            json.put("preferences", prefJson);
            json.put("userId", userPref.getUserId());
            
            return json;
        } catch (JSONException ex) {
            return null;
        }
    }
    
    public static IUserPreferences createSeekerPreferencesFromJsonObject(JSONObject jason) {
        try {
            SeekerPreferences seeker = new SeekerPreferences();
            JSONObject prefJson = jason.optJSONObject("preferences");
            
            seeker.setAnimals(prefJson.getInt("animals"));
            seeker.setKosher(prefJson.getInt("kosher"));
            seeker.setSharedExpences(prefJson.getInt("sharedExpences"));
            seeker.setSmoking(prefJson.getInt("smoking"));
            seeker.setVegan(prefJson.getInt("vegan"));
            seeker.setSeekerOccupation(prefJson.optString("seekerOccupation", null));
            seeker.setGayFriendly(prefJson.getInt("gayFriendly"));
            seeker.setMusicianFriendly(prefJson.getInt("musicianFriendly"));
            seeker.setNightLife(prefJson.getInt("nightLife"));
            
            JSONObject additionalDetails = jason.optJSONObject("additionalDetails");
            
            seeker.setAddress(additionalDetails.optString("address", null));
            seeker.setCity(additionalDetails.getString("city"));
            seeker.setNumberOfRoomates(additionalDetails.optInt("numberOfRoomates", 10));
            seeker.setMinPricePreffered(additionalDetails.optInt("minPricePreffered", 0));
            seeker.setMaxPricePreffered(additionalDetails.getInt("maxPricePreffered"));
            
            JSONObject mvp = jason.optJSONObject("mostValuablePreferences");
            if (mvp != null) {
                List<String> mvpList = new ArrayList<String>();
                
                if (mvp.getBoolean("animals")) {
                    mvpList.add("animals");
                }
                
                if (mvp.getBoolean("kosher")) {
                    mvpList.add("kosher");
                }
                
                if (mvp.getBoolean("sharedExpences")) {
                    mvpList.add("sharedExpences");
                }
                
                if (mvp.getBoolean("smoking")) {
                    mvpList.add("smoking");
                }
                
                if (mvp.getBoolean("vegan")) {
                    mvpList.add("vegan");
                }
                
                if (mvp.getBoolean("gayFriendly")) {
                    mvpList.add("gayFriendly");
                }
                
                if (mvp.getBoolean("musicianFriendly")) {
                    mvpList.add("musicianFriendly");
                }
                
                if (mvp.getBoolean("nightLife")) {
                    mvpList.add("nightLife");
                }
                
                seeker.setFirstValuablePref(mvpList.size() > 0? mvpList.get(0): null);
                seeker.setSecondValuablePref(mvpList.size() > 1? mvpList.get(1): null);
                seeker.setThirdValuablePref(mvpList.size() > 2? mvpList.get(2): null);
            }
            
            return seeker;
        } catch (JSONException ex) {
            return null;
        }
    }
    
    public static JSONObject createJsonObjectFromSeekerPreferences(SeekerPreferences userPref) {
        try {
            JSONObject json = new JSONObject();
            
            JSONObject prefJson = new JSONObject();
            
            prefJson.put("animals", userPref.getAnimals());
            prefJson.put("kosher", userPref.getKosher());
            prefJson.put("sharedExpences", userPref.getSharedExpences());
            prefJson.put("smoking", userPref.getSmoking());
            prefJson.put("vegan", userPref.getVegan());
            prefJson.put("gayFriendly", userPref.getGayFriendly());
            prefJson.put("musicianFriendly", userPref.getMusicianFriendly());
            prefJson.put("nightLife", userPref.getNightLife());
            prefJson.put("seekerOccupation", userPref.getSeekerOccupation());
            prefJson.put("address", userPref.getAddress());
            prefJson.put("city", userPref.getCity());
            prefJson.put("maxPricePreffered", userPref.getMaxPricePreffered());
            prefJson.put("minPricePreffered", userPref.getMinPricePreffered());
            prefJson.put("numberOfRoomates", userPref.getNumberOfRoomates());
            
            JSONObject mvp = new JSONObject();
            mvp.put("animals", checkIfPrefIsMVP("animals", userPref));
            mvp.put("kosher", checkIfPrefIsMVP("kosher", userPref));
            mvp.put("sharedExpences", checkIfPrefIsMVP("sharedExpences", userPref));
            mvp.put("smoking", checkIfPrefIsMVP("smoking", userPref));
            mvp.put("vegan", checkIfPrefIsMVP("vegan", userPref));
            mvp.put("gayFriendly", checkIfPrefIsMVP("gayFriendly", userPref));
            mvp.put("nightLife", checkIfPrefIsMVP("nightLife", userPref));
            mvp.put("musicianFriendly", checkIfPrefIsMVP("musicianFriendly", userPref));
            
            json.put("mostValuablePreferences", mvp);
            json.put("preferences", prefJson);
            json.put("userId", userPref.getUserId());
            
            return json;
        } catch (JSONException ex) {
            return null;
        }
    }

    private static boolean checkIfPrefIsMVP(String pref, IUserPreferences userPref) {
        String firstValuablePref = userPref.getFirstValuablePref();
        String secondValuablePref = userPref.getSecondValuablePref();
        String thirdValuablePref = userPref.getThirdValuablePref();
        
        return (firstValuablePref != null && firstValuablePref.equalsIgnoreCase(pref)) || 
                (secondValuablePref != null && secondValuablePref.equals(pref)) ||
                (thirdValuablePref != null && thirdValuablePref.equals(pref));
    }

    public static UserSettings getUserSettingsFromJsonObject(JSONObject userSettingsJson) {
        UserSettings userSe = new UserSettings();
        
        userSe.setEmail(userSettingsJson.optString("email", null));
        userSe.setMinIncludeMatch(userSettingsJson.optInt("minIncludeMatch", 60));
        userSe.setMinShowMatch(userSettingsJson.optInt("minShowMatch", 60));
                
        return userSe;
    }
}
