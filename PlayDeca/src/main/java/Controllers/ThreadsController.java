package Controllers;

import Models.Threads;
import Services.ThreadService;
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

    @Inject ThreadService ThreadService;
    private List<Threads> threadList;
    private Threads selectedThread;

    public ThreadsController() {
        threadList = new ArrayList<>();
        selectedThread = new Threads();

    }
    
    public List<Threads> getList() {
        if(!threadList.isEmpty()){
            return threadList;
        }
        return null;
    }
    
    @PostConstruct
    private void init(){
        threadList = ThreadService.listAll();
    }
    
    public Threads getThreadById(int threadId){
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
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().getTitle()));
    }

    
}
