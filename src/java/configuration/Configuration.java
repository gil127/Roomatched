package configuration;

public class Configuration {
    public class Prefferences {
        public static final int NUM_OF_PREFERENCES = 8;
        public static final int MIN_VALUE_OF_PREF = -2;
        public static final int MAX_VALUE_OF_PREF = 2;
    }
    
    public class Types {
        public static final String seekerType = "Seeker";
        public static final String offererType = "offerer";
    }
    
    public class PreffSelectionTableTypes {
        public static final int dontCare = 2;
        public static final int mvp = 1;
    }

    public class Table {
        public static final String USERS = "users";
        public static final String POSTS = "posts";
        public static final String APARTMENT_DETAILS = "apartmentdetails";
        public static final String GROUP_OF_SEEKERS = "groupofseekers";
        public static final String ROOM_DETAILS = "roomdetails";
        public static final String OFFERER_PREF = "offererpref";
        public static final String SEEKER_PREF = "seekerpref";
        public static final String USER_SETTINGS = "usersettings";
        public static final String FAVORITES = "favorites";
        public static final String CONVERSATION = "conversation";
        public static final String MESSAGES = "messages";
        public static final String FRIENDSHIP = "friendship";
        public static final String ERROR = "errorLog";
        public static final String USERS_PREFF_SELECTION = "usersPreffSelection";
    }
    
    public class Gender {
        public static final String MALE = "male";
        public static final String FEMALE = "female";
        public static final String ANY = "any";
    }
    
    public class MatchFactor {
        public static final double PASSING_BASIC_MATCH_PERCENTAGE_VALUE = 12.5;
        public static final double MAX_PERCENTAGE_VALUE_FOR_AGE_DIFFERENCE = 10;
        public static final int MAX_PERCENTAGE = 100;
        public static final int BONOS_PERCENTAGE_FOR_FACEBOOK_FRIENDS = 5;
        public static final int MIN_BLOCK_FOR_FIRST_LEVEL_OF_MUTUAL_FRIEND = 1;
        public static final int MAX_BLOCK_FOR_FIRST_LEVEL_OF_MUTUAL_FRIEND = 5;
        public static final int BONOS_PERCENTAGE_FOR_FIRST_LEVEL_OF_FACEBOOK_MUTUAL_FRIENDS = 2;
        public static final int MIN_BLOCK_FOR_SECOND_LEVEL_OF_MUTUAL_FRIEND = 6;
        public static final int MAX_BLOCK_FOR_SECOND_LEVEL_OF_MUTUAL_FRIEND = 9;
        public static final int BONOS_PERCENTAGE_FOR_SECOND_LEVEL_OF_FACEBOOK_MUTUAL_FRIENDS = 3;
        public static final int MIN_BLOCK_FOR_THIRD_LEVEL_OF_MUTUAL_FRIEND = 10;
        public static final int BONOS_PERCENTAGE_FOR_THIRD_LEVEL_OF_FACEBOOK_MUTUAL_FRIENDS = 4;
    }
    
    public static int NO_VALUE = -1;
}
