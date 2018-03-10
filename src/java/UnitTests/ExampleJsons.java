/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitTests;

import org.json.JSONException;
import org.json.JSONObject;
import pojos.interfaces.IUserPreferences;

/**
 *
 * @author Gil
 */
public class ExampleJsons {
    public static JSONObject getSeekerUserForCreation() throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"facebookId\":\"123456\",\"facebookFriends\":[\"10154075377768820\",\"10209204630567152\",\"10207336562995448\",\"1279295365436373\"],\"firstName\":\"Pavel\",\"lastName\":\"Asinovsky\",\"yearOfBirth\":null,\"sex\":\"male\",\"type\":\"SEEKER\",\"photoUrl\":\"https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xaf1/v/t1.0-1/p720x720/207155_4153306423861_584169503_n.jpg?oh=041389c7514956c54797a4d7442bdc78&oe=5881EDB0&__gda__=1481182941_cb45da2a613a079ec8b4d0181d7ac4cf\",\"preferences\":{\"smoking\":2,\"kosher\":-2,\"vegan\":0,\"sharedExpences\":-1,\"animals\":1,\"gayFriendly\":2,\"musicianFriendly\":2,\"nightLife\":1},\"mostValuablePreferences\":{\"smoking\":false,\"kosher\":false,\"vegan\":true,\"sharedExpences\":false,\"animals\":true,\"gayFriendly\":true,\"musicianFriendly\":false,\"nightLife\":false},\"additionalDetails\":{\"minPricePreffered\":1,\"maxPricePreffered\":2500,\"numberOfRoomates\":3,\"city\":\"Tel-Aviv\"}}");
        return new JSONObject(sb.toString());
    }
    
    public static JSONObject getUpdateSeekerUser() throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"preferences\":{\"smoking\":2,\"kosher\":-2,\"vegan\":0,\"sharedExpences\":2,\"animals\":1,\"gayFriendly\":-2,\"musicianFriendly\":2,\"nightLife\":1},\"mostValuablePreferences\":{\"smoking\":false,\"kosher\":false,\"vegan\":true,\"sharedExpences\":false,\"animals\":true,\"gayFriendly\":true,\"musicianFriendly\":false,\"nightLife\":false},\"additionalDetails\":{\"minPricePreffered\":1,\"maxPricePreffered\":2500,\"numberOfRoomates\":3,\"city\":\"Tel-Aviv\"}}");
        return new JSONObject(sb.toString());
    }

    static JSONObject getUserSettings() throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"minShowMatch\": 70 ,\"minIncludeMatch\": 80,\"email\":\"22123@gmail.com\"}");
        return new JSONObject(sb.toString());
    }

    static String getUserAbout() {
        return "I would love to see your apartment";
    }

    static JSONObject getOffererUserForCreation() throws JSONException {
        String str = "{\"facebookId\":\"12345678\",\"facebookFriends\":[\"10154069497413645\",\"10153916383232705\",\"10154454155259814\",\"10154535281591907\",\"10155128444279057\",\"10154499353152700\",\"10154075377768820\",\"10154555566139701\",\"10208522493312258\",\"10210061945402027\",\"10207702312815369\",\"10210756566412557\",\"10210559193641254\",\"10210113394338042\",\"10209255049365160\",\"10209137739193398\",\"1289351737741582\",\"1198383620182418\",\"320050101672345\"],\"firstName\":\"Or\",\"lastName\":\"Bar\",\"yearOfBirth\":1989,\"sex\":\"male\",\"type\":\"OFFERER\",\"photoUrl\":\"https://scontent.xx.fbcdn.net/v/t1.0-1/10429454_10205195925168074_3578062481703845349_n.jpg?oh=faf448807fd8b54875298d6988d03146&oe=584892CB\",\"preferences\":{\"smoking\":-1,\"kosher\":2,\"vegan\":-2,\"sharedExpences\":1,\"animals\":-1,\"gayFriendly\":0,\"musicianFriendly\":1,\"nightLife\":1},\"mostValuablePreferences\":{\"smoking\":true,\"kosher\":false,\"vegan\":true,\"sharedExpences\":true,\"animals\":false,\"gayFriendly\":false,\"musicianFriendly\":false,\"nightLife\":false},\"additionalDetails\":{\"roomDetails\":{\"price\":2500,\"size\":9,\"hasBalcony\":true,\"airConditioned\":true},\"apartmentDetails\":{\"city\":\"Tel-Aviv\",\"isRenovated\":true,\"hasElevator\":true,\"address\":\"Bugra 23\",\"size\":12,\"floor\":1,\"totalRooms\":3,\"totalRoomates\":2,\"freeRooms\":1},\"sexPreffered\":\"male\"}}";
        return new JSONObject(str);
    }

    static JSONObject getOffererUserPreff() throws JSONException {
        String str = "{\"preferences\":{\"smoking\":0,\"kosher\":2,\"vegan\":2,\"sharedExpences\":-1,\"animals\":-1,\"gayFriendly\":0,\"musicianFriendly\":1,\"nightLife\":1},\"mostValuablePreferences\":{\"smoking\":true,\"kosher\":false,\"vegan\":true,\"sharedExpences\":false,\"animals\":false,\"gayFriendly\":false,\"musicianFriendly\":false,\"nightLife\":true},\"additionalDetails\":{\"sexPreffered\":\"any\"}}";
        return new JSONObject(str);
    }

    static JSONObject getRoomDetails() throws JSONException {
        String str = "{\"additionalDetails\":{\"roomDetails\":{\"price\":2400,\"size\":12,\"hasBalcony\":false,\"airConditioned\":true}}}";
        return new JSONObject(str);
    }

    static JSONObject getApartmentDetails() throws JSONException {
        String str = "{\"additionalDetails\":{\"apartmentDetails\":{\"city\":\"Tel-Aviv\",\"isRenovated\":true,\"hasElevator\":false,\"address\":\"Bugrash 23\",\"size\":12,\"floor\":3,\"totalRooms\":4,\"totalRoomates\":2,\"freeRooms\":2}}}";
        return new JSONObject(str);
    }
}
