/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos.interfaces;

/**
 *
 * @author Gil
 */
public interface IUserPreferences {
    public int getUserId();

    public void setUserId(int id);

    public int getSmoking();

    public void setSmoking(int smoking);

    public int getSharedExpences();

    public void setSharedExpences(int sharedExpenses);

    public int getVegan();

    public void setVegan(int vegeterian);

    public int getKosher();

    public void setKosher(int kosherFood);

    public int getAnimals();

    public void setAnimals(int pets);
    
    public int getGayFriendly();
    
    public void setGayFriendly(int value);
    
    public int getMusicianFriendly();
    
    public void setMusicianFriendly(int value);
    
    public int getNightLife();
    
    public void setNightLife(int value);
    
    public String getFirstValuablePref();
    
    public void setFirstValuablePref(String value);
    
    public String getSecondValuablePref();
    
    public void setSecondValuablePref(String value);
    
    public String getThirdValuablePref();
    
    public void setThirdValuablePref(String value);
}
