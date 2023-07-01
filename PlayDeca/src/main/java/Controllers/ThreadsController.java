package Controllers;

import Models.Threads;
import Services.ThreadService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Al
 */
@Named(value = "ThreadsController")
@RequestScoped
public class ThreadsController implements Serializable{

    public ThreadsController() {
    }
    
    @Inject ThreadService ThreadService;
    
    List<Threads> List;
    
    public List<Threads> getList() {
        if(!List.isEmpty()){
            System.out.println(List.toString());
            return List;
        }
        return null;
    }
    
    @PostConstruct
    private void init(){
        List = ThreadService.listAll();
    }
    
    public Threads getThreadById(int threadId){
        return ThreadService.getThreadByID(threadId);
    }
    
}
