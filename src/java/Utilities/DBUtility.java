package Utilities;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUtility {
    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("NotesPU");

    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}
