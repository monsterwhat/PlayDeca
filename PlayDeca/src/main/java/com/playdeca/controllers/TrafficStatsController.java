package com.playdeca.controllers;

import com.playdeca.models.TrafficLog;
import com.playdeca.services.TrafficLogService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named(value = "TrafficStatsController")
@SessionScoped
public class TrafficStatsController implements Serializable {

    @Inject
    private TrafficLogService trafficLogService;

    private long totalPageViews;
    private long monthlyUniqueVisitors;
    private long allTimeUniqueVisitors;
    private double averageResponseTime;

    private Map<String, Long> deviceCounts;
    private Map<String, Long> osCounts;
    private Map<String, Long> browserCounts;
    private Map<String, Long> trafficByDate;
    private Map<String, Long> trafficSources;

    private List<TrafficLog> trafficLogs;
    private TrafficLog selectedLog;

    @PostConstruct
    public void init() {
        loadStatistics();
    }

    private void loadStatistics() {
        totalPageViews = trafficLogService.count();
        monthlyUniqueVisitors = trafficLogService.getUniqueVisitors(30);
        allTimeUniqueVisitors = trafficLogService.getUniqueVisitors();
        averageResponseTime = trafficLogService.getAverageResponseTime();

        deviceCounts = trafficLogService.getDeviceTypeCounts() != null ? trafficLogService.getDeviceTypeCounts() : new HashMap<>();
        osCounts = trafficLogService.getOSDistribution() != null ? trafficLogService.getOSDistribution() : new HashMap<>();
        browserCounts = trafficLogService.getBrowserDistribution() != null ? trafficLogService.getBrowserDistribution() : new HashMap<>();
        trafficByDate = trafficLogService.getTrafficVolumeByDate(30) != null ? trafficLogService.getTrafficVolumeByDate(30) : new HashMap<>();
        trafficSources = trafficLogService.getTrafficSources() != null ? trafficLogService.getTrafficSources() : new HashMap<>();

        trafficLogs = trafficLogService.findRecent(100);
        if (trafficLogs == null) {
            trafficLogs = new ArrayList<>();
        }
    }

    public void refresh() {
        loadStatistics();
    }

    public List<String> getDeviceLabels() {
        if (deviceCounts == null) return new ArrayList<>();
        return new ArrayList<>(deviceCounts.keySet());
    }

    public List<Long> getDeviceValues() {
        if (deviceCounts == null) return new ArrayList<>();
        return new ArrayList<>(deviceCounts.values());
    }

    public List<String> getOsLabels() {
        if (osCounts == null) return new ArrayList<>();
        return new ArrayList<>(osCounts.keySet());
    }

    public List<Long> getOsValues() {
        if (osCounts == null) return new ArrayList<>();
        return new ArrayList<>(osCounts.values());
    }

    public List<String> getBrowserLabels() {
        if (browserCounts == null) return new ArrayList<>();
        return new ArrayList<>(browserCounts.keySet());
    }

    public List<Long> getBrowserValues() {
        if (browserCounts == null) return new ArrayList<>();
        return new ArrayList<>(browserCounts.values());
    }

    public List<String> getTrafficDateLabels() {
        if (trafficByDate == null) return new ArrayList<>();
        List<String> sorted = new ArrayList<>(trafficByDate.keySet());
        Collections.sort(sorted);
        return sorted;
    }

    public List<Long> getTrafficDateValues() {
        if (trafficByDate == null) return new ArrayList<>();
        List<String> sorted = new ArrayList<>(trafficByDate.keySet());
        Collections.sort(sorted);
        List<Long> values = new ArrayList<>();
        for (String key : sorted) {
            values.add(trafficByDate.get(key));
        }
        return values;
    }

    public List<String> getSourceLabels() {
        if (trafficSources == null) return new ArrayList<>();
        return new ArrayList<>(trafficSources.keySet());
    }

    public List<Long> getSourceValues() {
        if (trafficSources == null) return new ArrayList<>();
        return new ArrayList<>(trafficSources.values());
    }

    public String getDeviceLabelsJson() {
        if (deviceCounts == null || deviceCounts.isEmpty()) return "";
        return deviceCounts.keySet().stream()
                .map(s -> "'" + s.replace("'", "\\'") + "'")
                .collect(Collectors.joining(","));
    }

    public String getDeviceValuesJson() {
        if (deviceCounts == null || deviceCounts.isEmpty()) return "";
        return deviceCounts.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public String getOsLabelsJson() {
        if (osCounts == null || osCounts.isEmpty()) return "";
        return osCounts.keySet().stream()
                .map(s -> "'" + s.replace("'", "\\'") + "'")
                .collect(Collectors.joining(","));
    }

    public String getOsValuesJson() {
        if (osCounts == null || osCounts.isEmpty()) return "";
        return osCounts.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public String getBrowserLabelsJson() {
        if (browserCounts == null || browserCounts.isEmpty()) return "";
        return browserCounts.keySet().stream()
                .map(s -> "'" + s.replace("'", "\\'") + "'")
                .collect(Collectors.joining(","));
    }

    public String getBrowserValuesJson() {
        if (browserCounts == null || browserCounts.isEmpty()) return "";
        return browserCounts.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public String getTrafficDateLabelsJson() {
        if (trafficByDate == null || trafficByDate.isEmpty()) return "";
        List<String> sorted = new ArrayList<>(trafficByDate.keySet());
        Collections.sort(sorted);
        return sorted.stream()
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(","));
    }

    public String getTrafficDateValuesJson() {
        if (trafficByDate == null || trafficByDate.isEmpty()) return "";
        List<String> sorted = new ArrayList<>(trafficByDate.keySet());
        Collections.sort(sorted);
        return sorted.stream()
                .map(trafficByDate::get)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public String getSourceLabelsJson() {
        if (trafficSources == null || trafficSources.isEmpty()) return "";
        return trafficSources.keySet().stream()
                .map(s -> "'" + s.replace("'", "\\'") + "'")
                .collect(Collectors.joining(","));
    }

    public String getSourceValuesJson() {
        if (trafficSources == null || trafficSources.isEmpty()) return "";
        return trafficSources.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public long getTotalPageViews() {
        return totalPageViews;
    }

    public long getMonthlyUniqueVisitors() {
        return monthlyUniqueVisitors;
    }

    public long getAllTimeUniqueVisitors() {
        return allTimeUniqueVisitors;
    }

    public double getAverageResponseTime() {
        return averageResponseTime;
    }

    public Map<String, Long> getDeviceCounts() {
        return deviceCounts;
    }

    public Map<String, Long> getOsCounts() {
        return osCounts;
    }

    public Map<String, Long> getBrowserCounts() {
        return browserCounts;
    }

    public Map<String, Long> getTrafficByDate() {
        return trafficByDate;
    }

    public Map<String, Long> getTrafficSources() {
        return trafficSources;
    }

    public List<TrafficLog> getTrafficLogs() {
        return trafficLogs;
    }

    public TrafficLog getSelectedLog() {
        return selectedLog;
    }

    public void setSelectedLog(TrafficLog selectedLog) {
        this.selectedLog = selectedLog;
    }
}
