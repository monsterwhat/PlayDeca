package Services;

import Models.Threads;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

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
    
    public Threads getThreadByID(int threadId){
        try {
            return em.find(Threads.class, threadId);
        } catch (Exception e) {
            System.out.println("Error: "+e.toString());
            return null;
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
            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    
}
