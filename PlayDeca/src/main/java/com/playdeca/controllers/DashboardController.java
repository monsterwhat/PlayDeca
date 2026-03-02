package com.playdeca.controllers;

import com.playdeca.models.ServerLogs;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Controller for managing admin dashboard view state
 */
@Named(value = "DashboardController")
@SessionScoped
public class DashboardController implements Serializable {

    private String view = "Home";
    private Object lineModel; // Simplified for now
    private List<ServerLogs> logsList;
    private ServerLogs selectedLog;

    public DashboardController() {
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String showHome() {
        this.view = "Home";
        return "";
    }

    public String showUsersTable() {
        this.view = "UsersTable";
        return "";
    }

    public String showUsersCreateForm() {
        this.view = "UsersCreateForm";
        return "";
    }

    public String showThreadsTable() {
        this.view = "ThreadsTable";
        return "";
    }

    public String showProductsTable() {
        this.view = "ProductsTable";
        return "";
    }

    public String showPostsTable() {
        this.view = "PostsTable";
        return "";
    }

    public String showOrdersTable() {
        this.view = "OrdersTable";
        return "";
    }

    public String showLogsForm() {
        this.view = "logsForm";
        return "";
    }

    public String showServerManagerForm() {
        this.view = "serverManagerForm";
        return "";
    }

    public String showTrafficStats() {
        this.view = "TrafficStats";
        return "";
    }

    public Object getLineModel() {
        return lineModel;
    }

    public void setLineModel(Object lineModel) {
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

    public String switchViewToForm(String viewName) {
        this.view = viewName;
        return "";
    }
}