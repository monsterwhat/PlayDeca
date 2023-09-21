package Controllers;

import Models.Threads;
import Services.ThreadService;
import Services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Al
 */

@Named(value = "ThreadsController")
@RequestScoped
public class ThreadsController implements Serializable{

    @Inject FacesContext facesContext;

    @Inject ThreadService ThreadService;
    @Inject UserService UserService;
    boolean isCacheValid = false;
    private List<Threads> threadList;
    private Threads selectedThread;
    private Threads newThread;
    private Long selectedUserId = 0L;

    public ThreadsController() {
    }

    @PostConstruct
    public void init(){
        threadList = new ArrayList<>();
        selectedThread = new Threads();
        newThread = new Threads();
    }
        
    public List<Threads> getList() {
        if(!isCacheValid){
            threadList =  ThreadService.listAll();
            isCacheValid = true;
        }
        return threadList;
    }
    
    private void invalidateCache() {
        selectedUserId = null;
        isCacheValid = false;
    }
    
    public void openNewThread(){
        newThread = new Threads();
    }
    
    public void deleteThread(){
        System.out.println("Thread: "+ selectedThread.getTitle());
        
        if(selectedThread != null){
            ThreadService.delete(selectedThread);
            invalidateCache();
        }else{
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error deleting thread."));
        }
    }
    
    public void createThread(){
        try {
            if(selectedUserId == 0L || selectedUserId == null){
                return;
            }
            newThread.setUser(UserService.getUserById(selectedUserId));
            
            if(newThread.getTitle() != null ){
                ThreadService.create(newThread);
                invalidateCache();

            }else{
                System.out.println("No User or Thread title provided");
            }
        } catch (Exception e) {
            System.out.println("Error creating Thread!" +e.getLocalizedMessage());
        }
        
    }
    
    public void saveThread(){
        if(selectedUserId == 0L || selectedUserId == null){
            return;
        }
        selectedThread.setUser(UserService.getUserById(selectedUserId));
        
        ThreadService.update(selectedThread);
        invalidateCache();
    }

    public Threads getThreadById(Long threadId){
        return ThreadService.getThreadByID(threadId);
    }
    
    public long getThreadCount(){
        return ThreadService.count();
    }

    public List<Threads> getThreadList() {
        return threadList;
    }

    public void setThreadList(List<Threads> threadList) {
        this.threadList = threadList;
    }

    public Threads getSelectedThread() {
        return selectedThread;
    }

    public void setSelectedThread(Threads selectedThread) {
        this.selectedThread = selectedThread;
    }
    
    public void onSelect(SelectEvent<Threads> event) {
        selectedThread = event.getObject();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thread Selected", event.getObject().getTitle()));
    }

    public Threads getNewThread() {
        return newThread;
    }

    public void setNewThread(Threads newThread) {
        this.newThread = newThread;
    }

    public long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
    
}
