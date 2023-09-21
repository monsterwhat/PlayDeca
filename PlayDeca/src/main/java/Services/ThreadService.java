package Services;

import Models.Threads;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author Al
 */
@Named
@Transactional
public class ThreadService extends GService<Threads>{
        
    @Override
    protected Class<Threads> getEntityClass(){
        return Threads.class;
    }
    
    public ThreadService() {
    }
    
    public Threads getThreadByID(Long threadId){
        try {
            return em.find(Threads.class, threadId);
        } catch (Exception e) {
            System.out.println("Error: "+e.toString());
            return null;
        }
    }
    
    @Override
    public void create(Threads thread) {
        try {
            
            thread.setDate(Date.from(Instant.now()));

            em.persist(thread);
            em.flush();

        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
        }
    }
    
    @Override
    public void delete(Threads thread) {
        try {
                        
            if (!em.contains(thread)) {
                thread = em.find(getEntityClass(), thread.getThreadId());
            }

            if (thread != null) {
                em.remove(thread);
                em.flush();

            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    
    @Override    
    public void update(Threads thread) {
        try {
            em.merge(thread);
            em.flush();

        } catch (Exception e) {
            
        }
    }

}
