/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.interfaces.IMatchesActions;
import utils.MySQLUtils;
import configuration.Configuration;
import dao.interfaces.IFriendshipActions;
import pojos.ApartmentDetails;
import pojos.MatchingUser;
import pojos.Post;
import pojos.OffererPreferences;
import pojos.SeekerPreferences;
import dao.interfaces.IPreferencesActions;
import pojos.RoomDetails;
import pojos.UserSettings;
import dao.interfaces.IUserSettingActions;
import pojos.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONObject;
import pojos.Friendship;
import pojos.MatchPreference;
import pojos.UsersPreffSelection;
import pojos.interfaces.IUserPreferences;
import utils.JSONObjectUtils;
import utils.QueriesUtils;

/**
 *
 * @author Gil
 */
public class MatchesActions implements IMatchesActions {

    private double numOfRelevantPreferences;
    private double preferencesValueInMatchPrecentage;

    @Override
    public List<JSONObject> getAllMatches(int id) {
        User userToMatch = (User) MySQLUtils.retrieveById(User.class, id);
        if (userToMatch != null) {
            return userToMatch.getType().equalsIgnoreCase(Configuration.Types.seekerType)
                    ? getMatchesForSeeker(userToMatch)
                    : getMatchesForOfferer(userToMatch);
        } else {
            return null;
        }
    }

    private List<JSONObject> getMatchesForSeeker(User seekerUser) {
        int seekerId = seekerUser.getId();
        IPreferencesActions seekerPrefActions = new SeekerPreferencesActions();
        SeekerPreferences seekerPref = (SeekerPreferences) seekerPrefActions.findUserPreferencesById(seekerId);

        IUserSettingActions userSettingsActions = new UserSettingsActions();
        UserSettings seekerSettings = userSettingsActions.findUserSettingsById(seekerId);

        String query = QueriesUtils.createQueryForSeekerMatches(seekerPref, seekerUser);
        List allRelevantOfferers = MySQLUtils.runQuery(query);

        Dictionary<Integer, RoomDetails> allMatchingRooms = new Hashtable<Integer, RoomDetails>();
        Dictionary<Integer, OffererPreferences> allMatchingOffererPref = new Hashtable<Integer, OffererPreferences>();
        List<MatchingUser> allMatchingUsers = new ArrayList<MatchingUser>();
        Dictionary<Integer, ApartmentDetails> allMatchingApartments = new Hashtable<Integer, ApartmentDetails>();
        Dictionary<Integer, Boolean> allMatchingUsersWithFavorites = new Hashtable<Integer, Boolean>();
        Dictionary<Integer, Boolean> allMatchingUsersWithPerfectFavorites = new Hashtable<Integer, Boolean>();
        Dictionary<Integer, Boolean> allMatchingUsersWithConversation = new Hashtable<Integer, Boolean>();
        Dictionary<Integer, Post> allMatchingPosts = new Hashtable<Integer, Post>();

        for (Object offerer : allRelevantOfferers) {
            Object[] offererDetails = (Object[]) offerer;

            OffererPreferences offererPref = (OffererPreferences) offererDetails[0];
            User offererUser = (User) offererDetails[1];
            RoomDetails room = (RoomDetails) offererDetails[2];
            ApartmentDetails apartment = (ApartmentDetails) offererDetails[3];
            UserSettings userSetting = (UserSettings) offererDetails[4];
            Post post = (Post) offererDetails[5];

            MatchingUser matchingUser = getMatchingUser(seekerUser, seekerPref, offererUser, offererPref, apartment.getCity(), true);

            if (matchingUser != null && checkIfMatchPrecentIsValid(matchingUser.getPrecentage(), seekerSettings, userSetting)) {
                allMatchingRooms.put(room.getId(), room);
                allMatchingApartments.put(apartment.getApartmentId(), apartment);
                allMatchingUsers.add(matchingUser);
                allMatchingOffererPref.put(offererPref.getUserId(), offererPref);
                allMatchingPosts.put(offererUser.getId(), post);

                boolean isFavorite = checkIfThereIsFavorite(seekerId, offererUser.getId());

                allMatchingUsersWithFavorites.put(offererUser.getId(), isFavorite);
                allMatchingUsersWithConversation.put(offererUser.getId(), checkIfThereIsConversation(offererUser, seekerUser));
                allMatchingUsersWithPerfectFavorites.put(offererUser.getId(), isFavorite && checkIfThereIsFavorite(offererUser.getId(), seekerId));
            }
        }

        return getListOfJsonObjectFromSeekerMatches(allMatchingPosts, allMatchingUsers, allMatchingRooms, allMatchingApartments, allMatchingOffererPref,
                allMatchingUsersWithFavorites, allMatchingUsersWithConversation, allMatchingUsersWithPerfectFavorites);
    }

    private boolean checkBasicMatch(User seekerUser, SeekerPreferences seeker, OffererPreferences offerer, String apartmentCity) {
        // Before we start to calcute the vectors prefernces, Let's check some basic match
        // for example if the seeker wants only female roomates and the current offer is a male, Let's save the calc
        boolean match = true;

        if (!seeker.getCity().equalsIgnoreCase(apartmentCity)) {
            match = false;
        }

        if (offerer.getSexPreffered() != null && !offerer.getSexPreffered().isEmpty()
                && !checkGenderIsValid(offerer.getSexPreffered(), seekerUser.getSex())) {
            match = false;
        }

        return match;
    }

    private boolean checkGenderIsValid(String sexPreffered, String sex) {
        return sexPreffered.equalsIgnoreCase(Configuration.Gender.ANY) || sexPreffered.equalsIgnoreCase(sex);
    }

    private MatchingUser getMatchingUser(User seekerUser, SeekerPreferences seekerPref, 
            User offererUser, OffererPreferences offererPref, String apartmentCity, boolean isSeekerMatch) {
        MatchingUser matchUser = null;
        if (checkBasicMatch(seekerUser, seekerPref, offererPref, apartmentCity)) {
            matchUser = calculateMatchPrecentage(seekerUser, seekerPref, offererUser, offererPref, isSeekerMatch);
        }
        return matchUser;
    }

    private boolean checkIfMatchPrecentIsValid(double matchPrecent, UserSettings userWhoWantMatch, UserSettings matchingUser) {
        return (matchPrecent >= userWhoWantMatch.getMinShowMatch()
                && matchPrecent >= matchingUser.getMinIncludeMatch());
    }

    private List<JSONObject> getMatchesForOfferer(User offererUser) {
        int offererId = offererUser.getId();

        String offererQuery = QueriesUtils.createQueryForOffererDetailsByUserId(offererId);
        Object[] offererDetails = (Object[]) MySQLUtils.runQuery(offererQuery).get(0);
        OffererPreferences offererPref = (OffererPreferences) offererDetails[0];
        RoomDetails room = (RoomDetails) offererDetails[1];
        ApartmentDetails apartment = (ApartmentDetails) offererDetails[2];
        UserSettings userSetting = (UserSettings) offererDetails[3];
        Post post = (Post) offererDetails[4];

        String query = QueriesUtils.createQueryForOffererMatch(offererPref, offererUser, room, apartment);
        List allRelevantOfferers = MySQLUtils.runQuery(query);

        Dictionary<Integer, SeekerPreferences> allMatchingSeekerPref = new Hashtable<Integer, SeekerPreferences>();
        Dictionary<Integer, Boolean> allMatchingUsersWithFavorites = new Hashtable<Integer, Boolean>();
        Dictionary<Integer, Boolean> allMatchingUsersWithConversation = new Hashtable<Integer, Boolean>();
        Dictionary<Integer, Boolean> allMatchingUsersWithPerfectFavorites = new Hashtable<Integer, Boolean>();
        List<MatchingUser> allMatchingUsers = new ArrayList<MatchingUser>();

        for (Object offerer : allRelevantOfferers) {
            Object[] seekerDetails = (Object[]) offerer;

            SeekerPreferences seekerPref = (SeekerPreferences) seekerDetails[0];
            User seekerUser = (User) seekerDetails[1];
            UserSettings seekerSetting = (UserSettings) seekerDetails[2];

            MatchingUser matchingUser = getMatchingUser(seekerUser, seekerPref, offererUser, offererPref, apartment.getCity(), false);

            if (matchingUser != null && checkIfMatchPrecentIsValid(matchingUser.getPrecentage(), userSetting, seekerSetting)) {
                allMatchingUsers.add(matchingUser);
                allMatchingSeekerPref.put(seekerPref.getUserId(), seekerPref);
                Boolean isFavorite = checkIfThereIsFavorite(offererId, seekerUser.getId());
                allMatchingUsersWithFavorites.put(seekerUser.getId(), isFavorite);
                allMatchingUsersWithConversation.put(seekerUser.getId(), checkIfThereIsConversation(offererUser, seekerUser));
                allMatchingUsersWithPerfectFavorites.put(seekerUser.getId(), isFavorite && checkIfThereIsFavorite(seekerUser.getId(), offererId));
            }
        }

        return getListOfJsonObjectFromOffererMatches(allMatchingUsers, allMatchingSeekerPref, allMatchingUsersWithConversation, allMatchingUsersWithFavorites, allMatchingUsersWithPerfectFavorites);
    }

    private List<JSONObject> getListOfJsonObjectFromSeekerMatches(Dictionary<Integer, Post> allMatchingPost,
            List<MatchingUser> allMatchingUsers,
            Dictionary<Integer, RoomDetails> allMatchingRooms,
            Dictionary<Integer, ApartmentDetails> allMatchingApartments,
            Dictionary<Integer, OffererPreferences> allRelevantOfferers,
            Dictionary<Integer, Boolean> allMatchingUsersWithFavorites,
            Dictionary<Integer, Boolean> allMatchingUsersWithConversation,
            Dictionary<Integer, Boolean> allMatchingUsersWithPerfectFavorites) {

        List<JSONObject> allMatches = new ArrayList<JSONObject>();
        
        allMatchingUsers.sort(new MatchingUser.matchingUserComparator());

        for (MatchingUser user : allMatchingUsers) {
            Post currPost = allMatchingPost.get(user.getUser().getId());
            OffererPreferences offererPref = allRelevantOfferers.get(currPost.getOffererId());
            RoomDetails room = allMatchingRooms.get(currPost.getRoomId());
            ApartmentDetails apartment = allMatchingApartments.get(room.getApartmentId());
            Boolean isFavorite = allMatchingUsersWithFavorites.get(currPost.getOffererId());
            Boolean hasConv = allMatchingUsersWithConversation.get(currPost.getOffererId());
            Boolean isPerfectFavorite = allMatchingUsersWithPerfectFavorites.get(currPost.getOffererId());
            JSONObject json = JSONObjectUtils.getSeekerMatchJsonObjectFromPojos(currPost.getPostId(), user, offererPref, room, apartment, isFavorite, hasConv, isPerfectFavorite);

            if (json != null) {
                allMatches.add(json);
            }
        }

        return allMatches;
    }

    private List<JSONObject> getListOfJsonObjectFromOffererMatches(List<MatchingUser> allMatchingUsers,
            Dictionary<Integer, SeekerPreferences> allMatchingSeekerPref,
            Dictionary<Integer, Boolean> allMatchingUsersWithConversation,
            Dictionary<Integer, Boolean> allMatchingUsersWithFavorites,
            Dictionary<Integer, Boolean> allMatchingUsersWithPerfectFavorites) {
        List<JSONObject> allMatches = new ArrayList<JSONObject>();
        
        allMatchingUsers.sort(new MatchingUser.matchingUserComparator());

        for (MatchingUser currUser : allMatchingUsers) {
            User user = currUser.getUser();
            SeekerPreferences seekerPref = allMatchingSeekerPref.get(user.getId());

            Boolean isFavorite = allMatchingUsersWithFavorites.get(user.getId());
            Boolean hasConv = allMatchingUsersWithConversation.get(user.getId());
            Boolean isPerfectFavorite = allMatchingUsersWithPerfectFavorites.get(user.getId());
            allMatches.add(JSONObjectUtils.getOffererMatchJsonObjectFromPojos(currUser, seekerPref, isFavorite, hasConv, isPerfectFavorite));
        }

        return allMatches;
    }

    private MatchingUser calculateMatchPrecentage(User seekerUser, SeekerPreferences seekerPref, 
            User offererUser, OffererPreferences offererPref, boolean  isSeekerMatch) {
        int yearOfBirthPreffCount = 1;
        if (seekerUser.getYearOfBirth() == Configuration.NO_VALUE || offererUser.getYearOfBirth() == Configuration.NO_VALUE) {
            yearOfBirthPreffCount = 0;
        }

        Map<String, MatchPreference> usersPrefDictionary = buildUsersPreferences(seekerPref, offererPref);

        List<String> seekerMvpPref = getUserMVPPref(seekerPref);
        List<String> offererMvpPref = getUserMVPPref(offererPref);

        // NOTE: For passing the basic match stage our users gets 15% match value
        // set factor
        numOfRelevantPreferences = (Configuration.Prefferences.NUM_OF_PREFERENCES + yearOfBirthPreffCount);
        preferencesValueInMatchPrecentage = (100 - Configuration.MatchFactor.PASSING_BASIC_MATCH_PERCENTAGE_VALUE);
        double regularPrefFactor = (preferencesValueInMatchPrecentage / numOfRelevantPreferences);

        calculateFactorOfPreferences(usersPrefDictionary, seekerMvpPref, offererMvpPref, regularPrefFactor);

        double difference = 0;

        // calc contrubution
        for (MatchPreference pref : usersPrefDictionary.values()) {
            difference += pref.getContribution();
        }

        // use facebook API's for metual friends + check if they are direct friends!
        List<String> mutualFacebookFriends = getMutualFacebookFriends(seekerUser.getFacebookId(), 
                offererUser.getFacebookId(), isSeekerMatch);
        boolean isFacebookFriends = false;

        User otherUser = isSeekerMatch? offererUser : seekerUser;
        if (mutualFacebookFriends.contains(otherUser.getFacebookId())) {
            mutualFacebookFriends.remove(otherUser.getFacebookId());
            isFacebookFriends = true;
        }
        
        int facebookFactor = getFacebookFriendsFactor(mutualFacebookFriends.size(), isFacebookFriends);
        
        double yearDiff = Math.abs(seekerUser.getYearOfBirth() != Configuration.NO_VALUE && offererUser.getYearOfBirth() != Configuration.NO_VALUE
                ? seekerUser.getYearOfBirth() - offererUser.getYearOfBirth() : 0);

        if (yearDiff * yearDiff >= regularPrefFactor) {
            difference += regularPrefFactor;
        } else {
            difference += yearDiff * yearDiff;
        }

        double matchPrecentage = Configuration.MatchFactor.MAX_PERCENTAGE - difference + facebookFactor;

        if (matchPrecentage < 0) {
            matchPrecentage = Configuration.MatchFactor.PASSING_BASIC_MATCH_PERCENTAGE_VALUE;
        } else if (matchPrecentage > Configuration.MatchFactor.MAX_PERCENTAGE) {
            matchPrecentage = Configuration.MatchFactor.MAX_PERCENTAGE;
        }
        
        return new MatchingUser(otherUser, matchPrecentage, isFacebookFriends, mutualFacebookFriends);
    }

    @Override
    public JSONObject getSeekerMatchDetailsByOffererAndSeeker(int seekerId, int offererId) {
        String offererQuery = QueriesUtils.createQueryForOffererDetailsByOffererId(offererId);
        List queryResults = MySQLUtils.runQuery(offererQuery);

        if (queryResults.isEmpty()) {
            return null;
        }

        Object[] offererDetails = (Object[]) queryResults.get(0);
        User offererUser = (User) offererDetails[0];
        OffererPreferences offererPref = (OffererPreferences) offererDetails[1];
        RoomDetails room = (RoomDetails) offererDetails[2];
        ApartmentDetails apartment = (ApartmentDetails) offererDetails[3];
        Post post = (Post) offererDetails[4];

        String seekerQuery = QueriesUtils.createQueryForSeekerDetailsByid(seekerId);
        queryResults = MySQLUtils.runQuery(seekerQuery);

        if (queryResults.isEmpty()) {
            return null;
        }

        Object[] seekerDetails = (Object[]) queryResults.get(0);
        User seekerUser = (User) seekerDetails[0];
        SeekerPreferences seekerPref = (SeekerPreferences) seekerDetails[1];

        MatchingUser matchingUser = getMatchingUser(seekerUser, seekerPref, offererUser, offererPref, apartment.getCity(), true);

        Boolean hasFavorite = checkIfThereIsFavorite(offererId, seekerId);

        Boolean hasConv = checkIfThereIsConversation(offererUser, seekerUser);

        Boolean isPerfectFavorite = hasFavorite && checkIfThereIsFavorite(seekerId, offererId);

        return JSONObjectUtils.getOffererMatchJsonObjectFromPojos(matchingUser, seekerPref, hasFavorite, hasConv, isPerfectFavorite);
    }

    @Override
    public JSONObject getOffererMatchDetailsByOffererAndSeeker(int seekerId, int offererId) {
        String offererQuery = QueriesUtils.createQueryForOffererDetailsByOffererId(offererId);
        List queryResults = MySQLUtils.runQuery(offererQuery);

        if (queryResults.isEmpty()) {
            return null;
        }

        Object[] offererDetails = (Object[]) queryResults.get(0);
        User offererUser = (User) offererDetails[0];
        OffererPreferences offererPref = (OffererPreferences) offererDetails[1];
        RoomDetails room = (RoomDetails) offererDetails[2];
        ApartmentDetails apartment = (ApartmentDetails) offererDetails[3];
        Post post = (Post) offererDetails[4];

        String seekerQuery = QueriesUtils.createQueryForSeekerDetailsByid(seekerId);
        queryResults = MySQLUtils.runQuery(seekerQuery);

        if (queryResults.isEmpty()) {
            return null;
        }

        Object[] seekerDetails = (Object[]) queryResults.get(0);
        User seekerUser = (User) offererDetails[0];
        SeekerPreferences seekerPref = (SeekerPreferences) seekerDetails[1];

        MatchingUser matchingUser = getMatchingUser(seekerUser, seekerPref, offererUser, offererPref, apartment.getCity(), false);

        Boolean hasFavorite = checkIfThereIsFavorite(seekerId, offererId);

        Boolean hasConv = checkIfThereIsConversation(offererUser, seekerUser);

        Boolean isPerfectFavorite = hasFavorite && checkIfThereIsFavorite(offererId, seekerId);

        return JSONObjectUtils.getSeekerMatchJsonObjectFromPojos(post.getPostId(), matchingUser, offererPref, room, apartment, hasFavorite, hasConv, isPerfectFavorite);
    }

    private List<String> getMutualFacebookFriends(String seekerFbUserId, String offererFbUserId, boolean isSeekerMatch) {
        List<Friendship> seekerFriends = new ArrayList<>();
        List<Friendship> offererFriends = new ArrayList<>();
        IFriendshipActions friendsActions = new FriendshipActions();

        seekerFriends = friendsActions.getUserFriendsList(seekerFbUserId);
        offererFriends = friendsActions.getUserFriendsList(offererFbUserId);
        
        List<String> allMutualFriends = new ArrayList<String>();
        
        for (Friendship seekerFriend : seekerFriends) {
            if (checkIfCurrentFriendIsTheOfferer(seekerFriend, offererFbUserId, seekerFbUserId)) {
                String otherFacebookId = isSeekerMatch? offererFbUserId : seekerFbUserId;
                allMutualFriends.add(otherFacebookId);
            } else {
                for (Friendship offererFriend : offererFriends) {
                    if (checkIfCurrentFriendIsMutual(seekerFriend, offererFriend, seekerFbUserId, offererFbUserId)) {
                        String mutualFriend = seekerFriend.getUserOnefacebookID().equalsIgnoreCase(seekerFbUserId)? 
                                seekerFriend.getUserTwofacebookID() : seekerFriend.getUserOnefacebookID();
                        allMutualFriends.add(mutualFriend);
                    }
                }
            }
        }

        return allMutualFriends;
    }
    
    private int getFacebookFriendsFactor(int countMutualFriends, boolean isTwoUsersAreFriends) {
        int mutualFriendsFactor = 0;
        if (isTwoUsersAreFriends) {
            mutualFriendsFactor = Configuration.MatchFactor.BONOS_PERCENTAGE_FOR_FACEBOOK_FRIENDS;
        } else {
            if (countMutualFriends >= Configuration.MatchFactor.MIN_BLOCK_FOR_FIRST_LEVEL_OF_MUTUAL_FRIEND
                    && countMutualFriends <= Configuration.MatchFactor.MAX_BLOCK_FOR_FIRST_LEVEL_OF_MUTUAL_FRIEND) {
                mutualFriendsFactor = Configuration.MatchFactor.BONOS_PERCENTAGE_FOR_FIRST_LEVEL_OF_FACEBOOK_MUTUAL_FRIENDS;
                
            } else if (countMutualFriends >= Configuration.MatchFactor.MIN_BLOCK_FOR_SECOND_LEVEL_OF_MUTUAL_FRIEND
                    && countMutualFriends <= Configuration.MatchFactor.MAX_BLOCK_FOR_SECOND_LEVEL_OF_MUTUAL_FRIEND) {
                
                mutualFriendsFactor = Configuration.MatchFactor.BONOS_PERCENTAGE_FOR_SECOND_LEVEL_OF_FACEBOOK_MUTUAL_FRIENDS;

            } else if (countMutualFriends >= Configuration.MatchFactor.MIN_BLOCK_FOR_THIRD_LEVEL_OF_MUTUAL_FRIEND) {
                mutualFriendsFactor = Configuration.MatchFactor.BONOS_PERCENTAGE_FOR_THIRD_LEVEL_OF_FACEBOOK_MUTUAL_FRIENDS;
            }
        }
        
        return mutualFriendsFactor;
    }

    private boolean checkIfCurrentFriendIsMutual(Friendship seekerFriend, Friendship offererFriend, String seekerFbUserId, String offererFbUserId) {
        boolean isMutual = false;

        String seekerFriendStr, offererFriendStr;

        if (seekerFriend.getUserOnefacebookID().equals(seekerFbUserId)) {
            seekerFriendStr = seekerFriend.getUserTwofacebookID();
        } else {
            seekerFriendStr = seekerFriend.getUserOnefacebookID();
        }

        if (offererFriend.getUserOnefacebookID().equals(offererFbUserId)) {
            offererFriendStr = offererFriend.getUserTwofacebookID();
        } else {
            offererFriendStr = offererFriend.getUserOnefacebookID();
        }

        //Now we got the 2 friend of the 2 useres... now we can check if they are equals.. ok
        isMutual = offererFriendStr.equals(seekerFriendStr) ? true : false;

        return isMutual;
    }

    private boolean checkIfCurrentFriendIsTheOfferer(Friendship seekerFriend, String offererFbUserId, String seekerFbUserId) {
        // now we got friend of the seeker, and we need to check if this is the offerer..
        String friendOfTheSeeker;

        if (seekerFriend.getUserOnefacebookID().equals(seekerFbUserId)) {
            friendOfTheSeeker = seekerFriend.getUserTwofacebookID();
        } else {
            friendOfTheSeeker = seekerFriend.getUserOnefacebookID();
        }

        return friendOfTheSeeker.equals(offererFbUserId);
    }

    private List<String> getUserMVPPref(IUserPreferences userPref) {

        List<String> userMvpPrefences = new ArrayList<>();

        if (userPref.getFirstValuablePref() != null) {
            userMvpPrefences.add(userPref.getFirstValuablePref());
        }
        if (userPref.getSecondValuablePref() != null) {
            userMvpPrefences.add(userPref.getSecondValuablePref());
        }
        if (userPref.getThirdValuablePref() != null) {
            userMvpPrefences.add(userPref.getThirdValuablePref());
        }

        return userMvpPrefences;
    }

    private Map<String, MatchPreference> buildUsersPreferences(SeekerPreferences seekerPref, OffererPreferences offererPref) {
        Map<String, MatchPreference> usersPref = new HashMap<>();

        usersPref.put("animals", new MatchPreference("animalFriendly", seekerPref.getAnimals(), offererPref.getAnimals()));
        usersPref.put("musicianFriendly", new MatchPreference("musicianFriendly", seekerPref.getMusicianFriendly(), offererPref.getMusicianFriendly()));
        usersPref.put("gayFriendly", new MatchPreference("gayFriendly", seekerPref.getGayFriendly(), offererPref.getGayFriendly()));
        usersPref.put("kosher", new MatchPreference("kosher", seekerPref.getKosher(), offererPref.getKosher()));
        usersPref.put("smoking", new MatchPreference("smoking", seekerPref.getSmoking(), offererPref.getSmoking()));
        usersPref.put("sharedExpences", new MatchPreference("sharedExpences", seekerPref.getSharedExpences(), offererPref.getSharedExpences()));
        usersPref.put("vegan", new MatchPreference("vegan", seekerPref.getVegan(), offererPref.getVegan()));
        usersPref.put("nightLife", new MatchPreference("nightLife", seekerPref.getNightLife(), offererPref.getNightLife()));

        return usersPref;
    }

    private void calculateFactorOfPreferences(Map<String, MatchPreference> usersPrefDictionary, List<String> seekerMvpPref,
            List<String> offererMvpPref, double regulerPrefFactor) {

        calcMvpPrefFactor(usersPrefDictionary, seekerMvpPref, offererMvpPref, regulerPrefFactor);
        
        calcPrefFactorByUsersPreffSelection(usersPrefDictionary, regulerPrefFactor);

        double allFactors = preferencesValueInMatchPrecentage;

        double handledFactors = 0;

        for (MatchPreference pref : usersPrefDictionary.values()) {
            handledFactors += pref.getFactor();
        }

        double allRestFactors = (allFactors - handledFactors) / numOfRelevantPreferences;

        for (MatchPreference pref : usersPrefDictionary.values()) {
            pref.addToFactor(allRestFactors);
        }
    }

    private void calcMvpPrefFactor(Map<String, MatchPreference> usersPrefDictionary, List<String> seekerMvpPref, List<String> offererMvpPref, double regularPrefFactor) {
        for (String mvpPref : offererMvpPref) {
            MatchPreference matchPref = usersPrefDictionary.get(mvpPref);
            if (matchPref != null) {
                matchPref.addToFactor(regularPrefFactor * 0.25);
            }
        }

        for (String mvpPref : seekerMvpPref) {
            MatchPreference matchPref = usersPrefDictionary.get(mvpPref);
            if (matchPref != null) {
                matchPref.addToFactor(regularPrefFactor * 0.25);
            }
        }
    }

    public static Boolean checkIfThereIsFavorite(int userId, int matchingUserId) {
        String query = QueriesUtils.getQueryForGetFavoriteOfUserAndMatchingUser(userId, matchingUserId);
        List favorite = MySQLUtils.runQuery(query);
        return !favorite.isEmpty();
    }

    private Boolean checkIfThereIsConversation(User offererUser, User seekerUser) {
        String query = QueriesUtils.createQueryForConversationIdByUsers(seekerUser.getId(), offererUser.getId());
        List conv = MySQLUtils.runQuery(query);
        return !conv.isEmpty();
    }

    private void calcPrefFactorByUsersPreffSelection(Map<String, MatchPreference> usersPrefDictionary, double regularFactor) {
        List<UsersPreffSelection> usersSeletectionFromDB = (List<UsersPreffSelection>) (List<?>) MySQLUtils.retrieveAll(UsersPreffSelection.class);
        
        UsersPreffSelection dontCareSelection = null, mvpSelection= null;
        
        for (UsersPreffSelection preffSelection : usersSeletectionFromDB) {
            if (preffSelection.getType() == Configuration.PreffSelectionTableTypes.dontCare) {
                dontCareSelection = preffSelection;
            } else {
                mvpSelection = preffSelection;
            }
        }
        
        if (mvpSelection == null || dontCareSelection == null) {
            return;
        }

        Map<String, Integer> dictionaryOfDontCareSelection = createDictionaryForDontCareSelection(dontCareSelection);
        
        dictionaryOfDontCareSelection = sortDictionaryByComparator(dictionaryOfDontCareSelection);
        
        changeFactorByUsersPreffSelection(usersPrefDictionary, regularFactor, dictionaryOfDontCareSelection, false);
        
        Map<String, Integer> dictionaryOfMVPSelection = createDictionaryForMVPSelection(mvpSelection);
        
        dictionaryOfMVPSelection = sortDictionaryByComparator(dictionaryOfMVPSelection);
        
        changeFactorByUsersPreffSelection(usersPrefDictionary, regularFactor, dictionaryOfMVPSelection, true);
        
    }
    
    private void changeFactorByUsersPreffSelection(Map<String, MatchPreference> usersPrefDictionary, double regularFactor,
            Map<String, Integer> dictionaryOfUserPreffSelection, boolean isMvpSelection) {
        int index = 1;
        int addOrSub = isMvpSelection? 1 : -1;
        for (Entry<String, Integer> entry : dictionaryOfUserPreffSelection.entrySet()) {
            if (index > numOfRelevantPreferences / 4) // we want to deal with only 1/4 of the high scored preferences
                break;
            
            usersPrefDictionary.get(entry.getKey()).addToFactor(regularFactor * 0.25 * addOrSub);
            index++;
        }
    }
    
    private Map<String, Integer> sortDictionaryByComparator(Map<String, Integer> unsortMap)
    {
        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                    return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
    
    private Map<String, Integer> createDictionaryForDontCareSelection(UsersPreffSelection dontCareSelection) {
        HashMap<String, Integer> res = new HashMap<>();
        
        res.put("animals", dontCareSelection.getAnimals());
        res.put("musicianFriendly", dontCareSelection.getMusicianFriendly());
        res.put("gayFriendly", dontCareSelection.getGayFriendly());
        res.put("kosher", dontCareSelection.getKosher());
        res.put("smoking", dontCareSelection.getSmoking());
        res.put("sharedExpences", dontCareSelection.getSharedExpences());
        res.put("vegan", dontCareSelection.getVegan());
        res.put("nightLife", dontCareSelection.getNightLife());
        
        return res;
    }

    private Map<String, Integer> createDictionaryForMVPSelection(UsersPreffSelection mvpSelection) {
        HashMap<String, Integer> res = new HashMap<>();
        
        res.put("animals", mvpSelection.getAnimals());
        res.put("musicianFriendly", mvpSelection.getMusicianFriendly());
        res.put("gayFriendly", mvpSelection.getGayFriendly());
        res.put("kosher", mvpSelection.getKosher());
        res.put("smoking", mvpSelection.getSmoking());
        res.put("sharedExpences", mvpSelection.getSharedExpences());
        res.put("vegan", mvpSelection.getVegan());
        res.put("nightLife", mvpSelection.getNightLife());
        
        return res;
    }
}