package dao;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.json.JSONObject;

/**
 *
 * @author or
 */
public class MapsActions {

    public List<JSONObject> getAllDistricts() {
        try {
            List<JSONObject> result = new ArrayList<JSONObject>();
            WebService.setUserName("roomatched"); // add your username here
            
            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setCountryCode("IL");
            searchCriteria.setFeatureCode("ADM1");
            searchCriteria.setMaxRows(1000);
            ToponymSearchResult searchResult = WebService.search(searchCriteria);
            if (searchResult != null && searchResult.getToponyms() != null) {
                for (Toponym toponym : searchResult.getToponyms()) {
                    JSONObject json = new JSONObject();
                    json.put("district", toponym.getName());
                    result.add(json);
                }
            }
            
            return result;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public List<JSONObject> getCitiesByDistrict(String district) {
        try {
            List<JSONObject> result = new ArrayList<JSONObject>();
            WebService.setUserName("roomatched"); // add your username here
            
            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setQ(district);
            searchCriteria.setCountryCode("IL");
            searchCriteria.setFeatureCode("PPL");
            searchCriteria.setMaxRows(1000);
            ToponymSearchResult searchResult = WebService.search(searchCriteria);
            if (searchResult != null && searchResult.getToponyms() != null) {
                for (Toponym toponym : searchResult.getToponyms()) {
                    JSONObject json = new JSONObject();
                        json.put("city", toponym.getName());
                        result.add(json);
                }
            }
            
            searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setQ(district);
            searchCriteria.setCountryCode("IL");
            searchCriteria.setFeatureCode("PPLA");
            searchCriteria.setMaxRows(1000);
            searchResult = WebService.search(searchCriteria);
            if (searchResult != null && searchResult.getToponyms() != null) {
                for (Toponym toponym : searchResult.getToponyms()) {
                    JSONObject json = new JSONObject();
                        json.put("city", toponym.getName());
                        result.add(json);
                }
            }
            
            return result;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public List<JSONObject> getNeighberhoodByCity(String city) {
        try {
            Toponym geoObject = getGeoNameByCityName(city);
            if (geoObject == null) {
                return null;
            }
            
            List<JSONObject> result = new ArrayList<JSONObject>();
            WebService.setUserName("roomatched"); // add your username here
            
            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setBoundingBox(geoObject.getBoundingBox());
            searchCriteria.setFeatureCode("PPLX");
            searchCriteria.setCountryCode("IL");
            searchCriteria.setMaxRows(1000);
            ToponymSearchResult searchResult = WebService.search(searchCriteria);
            if (searchResult != null && searchResult.getToponyms() != null) {
                for (Toponym toponym : searchResult.getToponyms()) {
                    JSONObject json = new JSONObject();
                        json.put("neighberhood", toponym.getName());
                        result.add(json);
                }
            }
            
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    private Toponym getGeoNameByCityName(String city) throws Exception {
        List<JSONObject> result = new ArrayList<JSONObject>();
        WebService.setUserName("roomatched"); // add your username here

        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
        searchCriteria.setCountryCode("IL");
        searchCriteria.setFeatureCode("PPLA");
        searchCriteria.setMaxRows(1000);
        ToponymSearchResult searchResult = WebService.search(searchCriteria);
        Toponym geoObject = null;
        if (searchResult != null && searchResult.getToponyms() != null) {
            for (Toponym toponym : searchResult.getToponyms()) {
                if (toponym.getName().equalsIgnoreCase(city))
                {
                    geoObject = toponym;
                    break;
                }
            }
        }
        
        if (geoObject == null) {
            searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setCountryCode("IL");
            searchCriteria.setFeatureCode("PPL");
            searchCriteria.setMaxRows(1000);
            searchResult = WebService.search(searchCriteria);
            if (searchResult != null && searchResult.getToponyms() != null) {
                for (Toponym toponym : searchResult.getToponyms()) {
                    if (toponym.getName().equalsIgnoreCase(city)) {
                        geoObject = toponym;
                        break;
                    }
                }
            }
        }

        return geoObject;
    }
}
