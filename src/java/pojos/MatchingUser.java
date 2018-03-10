package pojos;

import java.util.Comparator;
import java.util.List;
import pojos.User;

public class MatchingUser {
    private User user;
    private double matchPrecentage;
    private boolean isFacebookFriend;
    private List<String> listOfMutualFriends;
    
    public MatchingUser(User user, double matchPrec, boolean isFacebookFriend, List<String> numOfMutualFriednds) {
        this.user = user;
        this.matchPrecentage = matchPrec;
        this.isFacebookFriend = isFacebookFriend;
        this.listOfMutualFriends = numOfMutualFriednds;
    }
    
    public User getUser() {
        return user;
    }
    
    public double getPrecentage() {
        return matchPrecentage;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setPrecentage(double precentage) {
        this.matchPrecentage = precentage;
    }
    
    public boolean isFacebookFriend() {
        return isFacebookFriend;
    }
    
    public void setFacebookFriend(boolean value) {
        isFacebookFriend = value;
    }
    
    public List<String> getListOfMutualFriends() {
        return listOfMutualFriends;
    }
    
    public void setListOfMutualFriends(List<String> value) {
        listOfMutualFriends = value;
    }
    
    public static class matchingUserComparator implements Comparator<MatchingUser>
    {
        @Override
        public int compare(MatchingUser o1, MatchingUser o2) {
            return Double.compare(o2.getPrecentage(), o1.getPrecentage());
        }
    }
}
