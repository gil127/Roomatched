/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import pojos.ApartmentDetails;
import java.util.List;

/**
 *
 * @author Gil
 */
public interface IApartmentDetailsActions {
    public boolean deleteApartmentDetailsById(int id);
    public List<String> updateApartmentDetails(int id, ApartmentDetails apartment);
    public ApartmentDetails findApartmentDetailsById(int id);
    public void createApartmentDetails(ApartmentDetails apartment);
    public List<ApartmentDetails> getAllApartments();
}
