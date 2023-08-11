package Services;

import Models.ServerLogs;
import Models.Users;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.util.Date;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class LogsService extends GService<ServerLogs>{
    
    @Override
    protected Class<ServerLogs> getEntityClass(){
        return ServerLogs.class;
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
