package Data_Access;

import Utilities.DBUtility;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import Models.Note;
import Models.User;

public class NoteDB {

    public List<Note> getAll(String owner) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        
        try {
            User user = em.find(User.class, owner);
            return user.getNoteList();
        } finally {
            em.close();
        }
    }

    public Note get(int noteId) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        
        try {
            Note note = em.find(Note.class, noteId);
            return note;
        } finally { 
            em.close();
        }
    }

    public void insert(Note note) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            User user = note.getOwner();
            user.getNoteList().add(note);
            trans.begin();
            em.persist(note);
            em.merge(user);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public void update(Note note) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            em.merge(note);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public void delete(Note note) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            User user = note.getOwner();
            user.getNoteList().remove(note);
            trans.begin();
            em.remove(em.merge(note));
            em.merge(user);
            trans.commit();
        } catch (Exception ex) {
            trans.rollback();
        } finally {
            em.close();
        }
    }

}
