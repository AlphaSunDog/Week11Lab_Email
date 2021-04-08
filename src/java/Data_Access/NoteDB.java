package Data_Access;

import Utilities.DBUtility;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import Models.Note;
import Models.User;

public class NoteDB {

    public List<Note> getAll(String owner) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager(); //create emtity manager
        
        try {
            User user = em.find(User.class, owner); //search for user with em
            return user.getNoteList(); //return the users note
        } finally {
            em.close(); //close entity manager
        }
    }

    public Note get(int noteId) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager(); //create emtity manager
        
        try {
            Note note = em.find(Note.class, noteId); //search for specific note using noteId
            return note; //return note
        } finally { 
            em.close(); //close entity manager
        }
    }

    public void insert(Note note) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager(); //create emtity manager
        EntityTransaction trans = em.getTransaction();
        
        try {
            User user = note.getOwner(); //get the owner of the note
            user.getNoteList().add(note); //add the note to user
            trans.begin(); //begin transaction
            em.persist(note); //persist note
            em.merge(user);
            trans.commit(); //commit changes
        } catch (Exception ex) {
            trans.rollback(); //rollback if exception occurs
        } finally {
            em.close(); //close entity manager
        }
    }

    public void update(Note note) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager(); //create emtity manager
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin(); //begin transaction
            em.merge(note);
            trans.commit(); //commit changes
        } catch (Exception ex) {
            trans.rollback(); //rollback if exception occurs
        } finally {
            em.close(); //close entity manager
        }
    }

    public void delete(Note note) throws Exception {
        EntityManager em = DBUtility.getEmFactory().createEntityManager(); //create emtity manager
        EntityTransaction trans = em.getTransaction();
        
        try {
            User user = note.getOwner(); //get the owner of the note
            user.getNoteList().remove(note); //remove note from user
            trans.begin(); //begin transaction
            em.remove(em.merge(note));
            em.merge(user);
            trans.commit(); //commit changes
        } catch (Exception ex) {
            trans.rollback(); //rollback if exception occurs
        } finally {
            em.close(); //close entity manager
        }
    }
}
