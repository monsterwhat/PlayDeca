package com.playdeca.services;

import com.playdeca.models.ServerLogs;
import com.playdeca.models.Users;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Al
 */
@Named
@ApplicationScoped
public class LogsService {
    
    @Transactional
    public void createLog(String logName, String logDescription, Users user) {
        try {
            ServerLogs log = new ServerLogs();
            log.setName(logName);
            log.setDescription(logDescription);
            log.setUser(user);
            log.setRegistrationDate(LocalDateTime.now());
            log.persist();
        } catch (Exception e) {
            System.out.println("Error creating log: " + e.getMessage());
        }
    }
    
    public long count() {
        return ServerLogs.count();
    }
}