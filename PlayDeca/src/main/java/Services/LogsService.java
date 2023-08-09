package Services;

import Models.ServerLogs;
import Models.Users;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class LogsService implements Serializable{
    @PersistenceContext EntityManager em;

    public List<ServerLogs> listAll() {
        try {
            TypedQuery<ServerLogs> query = em.createQuery("SELECT l FROM ServerLogs l ORDER BY l.date DESC", ServerLogs.class);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public void createLog(String LogName, String LogDescription, Users User) {
        ServerLogs log = new ServerLogs();
        log.setName(LogName);
        log.setDescription(LogDescription);
        log.setUser(User);
        log.setDate(new Date());
        em.persist(log);
    }

}
