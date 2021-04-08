package Data_Access;

import Utilities.DBUtility;
import Models.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import Models.Role;

public class UserDB {

    public int insert(User user) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            // Give user a role (3 is regular user)
            Role role = em.find(Role.class, 3);
            user.setRole(role);
            
            trans.begin(); //begin transaction
            em.persist(user);
            trans.commit(); //commit changes
            return 1;
        } catch (Exception ex) {
            trans.rollback(); //rollback to fix the exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, "Cannot insert " + user.toString(), ex); //write a note in logger about the insert exception
            throw new Exception("Error inserting user");
        } finally {
            em.close(); //close entity manager
        }
    }

    public int update(User user) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin(); //begin transaction
            em.merge(user);
            trans.commit(); //commit changes
            return 1;
        } catch (Exception ex) {
            trans.rollback(); //rollback to fix the exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, "Cannot update " + user.toString(), ex); //write a note in logger about the update exception
            throw new Exception("Error updating user");
        } finally {
            em.close(); //close entity manager
        }
    }

    public List<User> getAll() throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        try {
            List<User> users = em.createNamedQuery("User.findAll", User.class).getResultList();
            return users;                
        } finally {
            em.close();
        }
    }

    public User getUser(String username) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        try {
            User user = em.find(User.class, username);
            return user;
        } finally {
            em.close();
        }
    }

    public int delete(User user) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin(); //begin transaction
            em.remove(em.merge(user));
            trans.commit(); //commit changes
            return 1;
        } catch (Exception ex) {
            trans.rollback(); //rollback to fix the exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, "Cannot delete " + user.toString(), ex); //write a note in logger about the delete exception
            throw new Exception("Error deleting user");
        } finally {
            em.close();
        }
    }
    
     public User getUserByEmail(String email){
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
         User user = em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getSingleResult();
         return user;
     }
    
    public User getUserByUUID(String uuid){
         EntityManager em = DBUtility.getEmFactory().createEntityManager();
         User user = em.createNamedQuery("User.findByResetPasswordUUID", User.class).setParameter("resetPasswordUUID", uuid).getSingleResult();
         return user;
    }
}
