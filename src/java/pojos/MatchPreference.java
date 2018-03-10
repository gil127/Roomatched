/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import configuration.Configuration;

/**
 *
 * @author Gil
 */
public class MatchPreference {
    
    private String namePreference;
    private int seekerPrefValue;
    private int offererPrefValue;
    private double factor = 0;
    private double contribution = Configuration.NO_VALUE;
    
    public MatchPreference(String namePreference, int seekerPrefValue, int offerPrefValue) {
        this.namePreference = namePreference;
        this.seekerPrefValue = seekerPrefValue;
        this.offererPrefValue = offerPrefValue;
    }
    
    public MatchPreference(String namePreference, int seekerPrefValue, int offerPrefValue, double factor) {
        this(namePreference, seekerPrefValue, offerPrefValue);
        this.factor = factor;
    }
    
    public MatchPreference(String namePreference, int seekerPrefValue, int offerPrefValue, double factor, double contrubution) {
        this(namePreference, seekerPrefValue, offerPrefValue, factor);
        this.contribution = contrubution;
    }
    
    public String getNamePreference() {
        return this.namePreference;
    }
    
    public void setNamePreference(String value) {
        this.namePreference = value;
    }
    
    public int getSeekerPrefValue() {
        return this.seekerPrefValue;
    }
    
    public void setSeekerPrefValue(int value) {
        this.seekerPrefValue = value;
    }
    
    public int getOffererPrefValue() {
        return this.offererPrefValue;
    }
    
    public void setOffererPrefValue(int value) {
        this.seekerPrefValue = value;
    }
    
    public double getFactor() {
       return this.factor; 
    }
    
    public void setFactor(double value) {
        this.factor = value;
    }
    
    public void addToFactor(double value) {
        this.factor += value;
    }
    
    public double getContribution() {
        if (contribution == Configuration.NO_VALUE) {
            calcContribution();
        }
        
        return this.contribution;
    }
    
    public void setContribution(double value) {
        this.contribution = value;
    }
    
    private void calcContribution() {
        double diff = Math.abs(seekerPrefValue != 0 && offererPrefValue != 0
         ? seekerPrefValue - offererPrefValue : ((seekerPrefValue - offererPrefValue) * (double) 0.75));
        
        double fac = factor / (Configuration.Prefferences.MAX_VALUE_OF_PREF - Configuration.Prefferences.MIN_VALUE_OF_PREF);
        
        contribution = fac * diff;
    }
}
