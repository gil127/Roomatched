package dao;

import dao.interfaces.IApartmentDetailsActions;
import utils.MySQLUtils;
import pojos.ApartmentDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ApartmentActions implements IApartmentDetailsActions {
    public static List<String> checkValidation(ApartmentDetails apartment) {
        List<String> errors = new ArrayList<String>();
        
        if (apartment != null) {
          
            if (apartment.getAddress().trim().length() <= 0) {
                errors.add("The apartment adderss can't be empty");
            }
            if (apartment.getCity().trim().length() <= 0) {
                errors.add("The city of the apartment can't be empty");
            }
        } else {
            errors.add("The json object of the apartmentDetails is incorrect.");
        }
        
        return errors;
    }

    @Override
    public boolean deleteApartmentDetailsById(int id) {
        ApartmentDetails apartment = (ApartmentDetails) MySQLUtils.retrieveById(ApartmentDetails.class, id);
        if (apartment != null) {
            MySQLUtils.delete(apartment);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> updateApartmentDetails(int id, ApartmentDetails apartment) {
        List<String> errors = checkValidation(apartment);
        
        if (errors != null && !errors.isEmpty()) {
            return errors;
        }
        
        ApartmentDetails apartmentFromDB = (ApartmentDetails) MySQLUtils.retrieveById(ApartmentDetails.class, id);
        if (apartmentFromDB != null) {
            apartment.setApartmentId(apartmentFromDB.getApartmentId());
            MySQLUtils.Update(apartment);
            return null;
        } else {
            errors = new ArrayList<String>();
            errors.add("There is no apartment with apartmentId " + id);
            return errors;
        }
    }

    @Override
    public ApartmentDetails findApartmentDetailsById(int id) {
        ApartmentDetails apartment = (ApartmentDetails) MySQLUtils.retrieveById(ApartmentDetails.class ,id);
        if (apartment != null) {
            return apartment;
        } else {
            return null;
        } 
    }

    @Override
    public void createApartmentDetails(ApartmentDetails apartment) {        
        try {
            EntityManager em = MySQLUtils.getEntityManager();
            MySQLUtils.beginTransaction(em);
            MySQLUtils.persist(apartment, em);
            MySQLUtils.commitTransaction(em);
        } catch (Exception ex) {
            Logger.getLogger(ApartmentActions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ApartmentDetails> getAllApartments() {
        List<ApartmentDetails> apartments = (List<ApartmentDetails>)(List<?>) MySQLUtils.retrieveAll(ApartmentDetails.class);
        if (apartments.isEmpty()) {
            return null;
        } else {
            return apartments;
        } 
    }
}
