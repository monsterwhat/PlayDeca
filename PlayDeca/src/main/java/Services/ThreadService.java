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
    
    @PostConstruct
    void init(){
    }
    
    public Threads getThreadByID(int threadId){
        try {
            return em.find(Threads.class, threadId);
        } catch (Exception e) {
            System.out.println("Error: "+e.toString());
            return null;
        }
    }
    
}
