package utils;

import pojos.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class MySQLUtils {
    private static final String DBName = "RoomatchedPU";
    private static final EntityManagerFactory emf;
    
    static {
       emf = javax.persistence.Persistence.createEntityManagerFactory(DBName);
    }
    
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static void beginTransaction(EntityManager em) {
        // Transaction start
        em.getTransaction().begin();
    }
    
    public static String commitTransaction(EntityManager em) throws Exception{
        try {
            EntityTransaction transaction = em.getTransaction();
            transaction.commit();
            em.close(); 
            return null;
        }   catch (Exception ex) {
            throw ex;
        }
    }
    
    public static Object persist(Object object, EntityManager em) throws Exception {
        try {
            em.persist(object);
            em.flush();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            em.close();
            throw e;
        }
    }
    
    public static Object retrieveById(Class c, int id) {
        EntityManager em = emf.createEntityManager();
        return em.find(c, id);
    }
    
    public static User retrieveUserByFacebookId(Class c, String id) {
        EntityManager em = emf.createEntityManager();
        List<Object> listOfObjects;

        em.getTransaction().begin();
        Query q = em.createQuery("SELECT c FROM " + c.getName() + " c WHERE c.facebookId = '" + id + "'");
        listOfObjects = q.getResultList();

        em.getTransaction().commit();
        em.close();

        return listOfObjects.isEmpty() ? null : (User)listOfObjects.get(0);
    }

    public static List<Object> retrieveAll(Class c) {
        EntityManager em = emf.createEntityManager();
        List<Object> listOfObjects;

        em.getTransaction().begin();
        Query q = em.createQuery("SELECT c FROM " + c.getName() + " c");
        listOfObjects = q.getResultList();

        em.getTransaction().commit();
        em.close();

        return listOfObjects;
    }

    public static String updateSetString(String[] columnNames, Object[] values) {
        StringBuilder qString = new StringBuilder();

        for (int i = 0; i < columnNames.length; i++) {
            if (values[i] != null && !(values[i] instanceof Integer) && !(values[i] instanceof Boolean)) {
                qString.append(columnNames[i])
                        .append("=\"")
                        .append(values[i]).append("\"");

                qString.append(",");
            } else {
                qString.append(columnNames[i])
                        .append("=")
                        .append(values[i]);

                qString.append(",");
            }

        }

        qString.setLength(qString.length() - 1); // Trim last ',' char

        return qString.toString();
    }

    public static void delete(Object object) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        object = em.merge(object);
        em.remove(object);
        em.getTransaction().commit();
        em.close();
    }

    public static void Update(Object object) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(object);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void Update(Object object, EntityManager em) {
        em.merge(object);
    }
    
    public static User getUserByMailAndPass(Class c, String mail, String password) {
        EntityManager em = emf.createEntityManager();
        List<Object> listOfObjects;

        em.getTransaction().begin();
        Query q = em.createQuery("SELECT c FROM " + c.getName() + " c WHERE c.email = '" + mail + "' and c.password = '" + password + "'");
        listOfObjects = q.getResultList();

        em.getTransaction().commit();
        em.close();

        return listOfObjects.isEmpty() ? null : (User)listOfObjects.get(0);
    }

    public static boolean emailExist(Class c, String email) {
        EntityManager em = emf.createEntityManager();
        List<Object> listOfObjects;

        em.getTransaction().begin();
        Query q = em.createQuery("SELECT c FROM " + c.getName() + " c WHERE c.email = '" + email + "'");
        listOfObjects = q.getResultList();

        em.getTransaction().commit();
        em.close();

        return !listOfObjects.isEmpty();
    }
    
    public static List runQuery(String sqlQuery) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery(sqlQuery);
        return query.getResultList();
    }
    
    public static void runDeleteQuery(String sqlQuery) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery(sqlQuery);
        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
}
