package Controllers;

import Models.ServerLogs;
import Services.LogsService;
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
    
    @Inject
    LogsService logService;
    
    @PostConstruct
    public void init() {
        createLineModel();
        loadLogs();
    }
    
    public void loadLogs(){
        logsList = new ArrayList<>();
        logsList = logService.listAll();
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

}
