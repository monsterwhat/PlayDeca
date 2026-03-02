package com.playdeca.services;

import com.playdeca.models.TrafficLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ApplicationScoped
public class TrafficLogService {

    @Transactional
    public void create(TrafficLog log) {
        try {
            log.persist();
        } catch (Exception e) {
            System.out.println("Error creating traffic log: " + e.getMessage());
        }
    }

    public long count() {
        return TrafficLog.count();
    }

    public List<TrafficLog> findAll() {
        return TrafficLog.<TrafficLog>list("ORDER BY visitTime DESC");
    }

    public List<TrafficLog> findRecent(int limit) {
        List<TrafficLog> all = TrafficLog.<TrafficLog>list("ORDER BY visitTime DESC");
        return all.stream().limit(limit).collect(java.util.stream.Collectors.toList());
    }

    public Map<String, Long> getDeviceTypeCounts() {
        List<TrafficLog> logs = findAll();
        Map<String, Long> counts = new HashMap<>();
        for (TrafficLog log : logs) {
            String device = log.getDeviceType() != null ? log.getDeviceType() : "Unknown";
            counts.put(device, counts.getOrDefault(device, 0L) + 1);
        }
        return counts;
    }

    public Map<String, Long> getTrafficVolumeByDate() {
        List<TrafficLog> logs = findAll();
        Map<String, Long> volume = new HashMap<>();
        for (TrafficLog log : logs) {
            if (log.getVisitTime() != null) {
                String date = log.getVisitTime().toLocalDate().toString();
                volume.put(date, volume.getOrDefault(date, 0L) + 1);
            }
        }
        return volume;
    }

    public Map<String, Long> getTrafficVolumeByDate(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        List<TrafficLog> logs = TrafficLog.<TrafficLog>list("visitTime >= ?1 ORDER BY visitTime", cutoff);
        Map<String, Long> volume = new HashMap<>();
        for (TrafficLog log : logs) {
            if (log.getVisitTime() != null) {
                String date = log.getVisitTime().toLocalDate().toString();
                volume.put(date, volume.getOrDefault(date, 0L) + 1);
            }
        }
        return volume;
    }

    public Map<String, Long> getOSDistribution() {
        List<TrafficLog> logs = findAll();
        Map<String, Long> counts = new HashMap<>();
        for (TrafficLog log : logs) {
            String os = log.getOperatingSystem() != null ? log.getOperatingSystem() : "Unknown";
            counts.put(os, counts.getOrDefault(os, 0L) + 1);
        }
        return counts;
    }

    public Map<String, Long> getBrowserDistribution() {
        List<TrafficLog> logs = findAll();
        Map<String, Long> counts = new HashMap<>();
        for (TrafficLog log : logs) {
            String browser = log.getBrowserVersion() != null ? log.getBrowserVersion() : "Unknown";
            counts.put(browser, counts.getOrDefault(browser, 0L) + 1);
        }
        return counts;
    }

    public Map<String, Double> getResponseTimeAnalysis() {
        List<TrafficLog> logs = findAll();
        Map<String, List<Long>> pageTimes = new HashMap<>();
        for (TrafficLog log : logs) {
            if (log.getPageUrl() != null && log.getResponseTime() != null) {
                String page = log.getPageUrl();
                pageTimes.computeIfAbsent(page, k -> new java.util.ArrayList<>()).add(log.getResponseTime());
            }
        }
        Map<String, Double> averages = new HashMap<>();
        for (Map.Entry<String, List<Long>> entry : pageTimes.entrySet()) {
            double avg = entry.getValue().stream().mapToLong(Long::longValue).average().orElse(0.0);
            averages.put(entry.getKey(), avg);
        }
        return averages;
    }

    public Map<String, Long> getTrafficSources() {
        List<TrafficLog> logs = findAll();
        Map<String, Long> sources = new HashMap<>();
        for (TrafficLog log : logs) {
            String referrer = log.getReferrerUrl();
            if (referrer == null || referrer.isEmpty()) {
                referrer = "Direct";
            } else if (referrer.contains("google")) {
                referrer = "Google";
            } else if (referrer.contains("discord")) {
                referrer = "Discord";
            } else if (referrer.contains("facebook")) {
                referrer = "Facebook";
            } else if (referrer.contains("twitter") || referrer.contains("x.com")) {
                referrer = "Twitter/X";
            } else {
                referrer = "Other";
            }
            sources.put(referrer, sources.getOrDefault(referrer, 0L) + 1);
        }
        return sources;
    }

    public long getUniqueVisitors() {
        List<TrafficLog> logs = findAll();
        return logs.stream().map(TrafficLog::getIpAddress).distinct().count();
    }

    public long getUniqueVisitors(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        List<TrafficLog> logs = TrafficLog.<TrafficLog>list("visitTime >= ?1", cutoff);
        return logs.stream().map(TrafficLog::getIpAddress).distinct().count();
    }

    public double getAverageResponseTime() {
        List<TrafficLog> logs = findAll();
        return logs.stream()
                .filter(l -> l.getResponseTime() != null)
                .mapToLong(TrafficLog::getResponseTime)
                .average()
                .orElse(0.0);
    }

    public Map<Integer, List<Long>> getAverageResponseTimeByHour() {
        List<TrafficLog> logs = findAll();
        Map<Integer, List<Long>> hourlyTimes = new HashMap<>();
        for (TrafficLog log : logs) {
            if (log.getVisitTime() != null && log.getResponseTime() != null) {
                int hour = log.getVisitTime().getHour();
                hourlyTimes.computeIfAbsent(hour, k -> new java.util.ArrayList<>()).add(log.getResponseTime());
            }
        }
        return hourlyTimes;
    }

    public List<TrafficLog> findByIpAddress(String ipAddress) {
        return TrafficLog.<TrafficLog>list("ipAddress = ?1 ORDER BY visitTime DESC", ipAddress);
    }

    public List<TrafficLog> findByPageUrl(String pageUrl) {
        return TrafficLog.<TrafficLog>list("pageUrl LIKE ?1 ORDER BY visitTime DESC", "%" + pageUrl + "%");
    }

    public List<TrafficLog> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return TrafficLog.<TrafficLog>list("visitTime >= ?1 AND visitTime <= ?2 ORDER BY visitTime DESC", start, end);
    }
}
