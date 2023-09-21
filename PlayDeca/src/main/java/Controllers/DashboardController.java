package Controllers;

import Models.ServerLogs;
import Services.LogsService;
import Utils.ServerInfo;
import Utils.ServerInfoStorage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;

/**
 *
 * @author Al
 */

@Named(value="DashboardController")
@ViewScoped
@RolesAllowed(value = "admin")
public class DashboardController implements Serializable{
    
    private LineChartModel lineModel;
    private List<ServerLogs> logsList;
    private ServerLogs selectedLog;
    private List<ServerInfo> serverInfoStats;
    private List<String> Plugins;
    private String view;
    
    @Inject LogsService logService;
    @Inject ServerInfoStorage serverStats;

    public DashboardController() {
    }

    @PostConstruct
    public void init() {
        createLineModel();
        loadData();
        view="Home";
    }

    public void switchViewToForm(String FormName) {
        switch (FormName) {
            case "UsersTable" -> {
                view="UsersTable";
            }
            case "UsersUpdateForm" -> {
                view="UsersUpdateForm";
            }
            case "UsersCreateForm" -> {
                view="UsersCreateForm";
            }
            case "ThreadsTable" -> {
                view="ThreadsTable";
            }
            case "ThreadsUpdateForm" -> {
                view="ThreadsUpdateForm";
            }
            case "ThreadsCreateForm" -> {
                view="ThreadsCreateForm";
            }
            case "PostsTable" -> {
                view="PostsTable";
            }
            case "PostsUpdateForm" -> {
                view="PostsUpdateForm";
            }
            case "PostsCreateForm" -> {
                view="PostsCreateForm";
            }
            case "ProductsTable" -> {
                view="ProductsTable";
            }
            case "ProductsUpdateForm" -> {
                view="ProductsUpdateForm";
            }
            case "ProductsCreateForm" -> {
                view="ProductsCreateForm";
            }
            case "OrdersTable" -> {
                view="OrdersTable";
            }
            case "OrdersUpdateForm" -> {
                view="OrdersUpdateForm";
            }
            case "OrdersCreateForm" -> {
                view="OrdersCreateForm";
            }
            default ->
                view="Home";
            }
    }
        
    public void loadData(){
        logsList = new ArrayList<>();
        logsList = logService.listAll();
        serverInfoStats = serverStats.getServerInfoHistory();
    }
    
    public void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        values.add(65);
        values.add(59);
        values.add(80);
        values.add(81);
        values.add(56);
        values.add(55);
        values.add(40);
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Online Players");
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);

        List<String> labels = new ArrayList<>();
        labels.add("Monday");
        labels.add("Tuesday");
        labels.add("Wednesday");
        labels.add("Thursday");
        labels.add("Friday");
        labels.add("Saturday");
        labels.add("Sunday");
        data.setLabels(labels);

        lineModel.setData(data);
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    public List<ServerLogs> getLogsList() {
        return logsList;
    }

    public void setLogsList(List<ServerLogs> logsList) {
        this.logsList = logsList;
    }

    public ServerLogs getSelectedLog() {
        return selectedLog;
    }

    public void setSelectedLog(ServerLogs selectedLog) {
        this.selectedLog = selectedLog;
    }

    public String getView() {
        return view;
    }

    public void setView(String View) {
        this.view = View;
    }
    
    

}
