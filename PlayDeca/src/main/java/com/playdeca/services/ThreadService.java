package com.playdeca.services;

import com.playdeca.models.Threads;
import com.playdeca.models.Users;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Al
 */
@Named
@ApplicationScoped
public class ThreadService {
    
    @Transactional
    public void create(Threads thread) {
        try {
            thread.setDate(LocalDateTime.now());
            thread.persist();
        } catch (Exception e) {
            System.out.println("Error creating thread: " + e.getMessage());
        }
    }
    
    @Transactional
    public void update(Threads thread) {
        try {
            Threads existing = Threads.findById(thread.getThreadId());
            if (existing != null) {
                existing.setUser(thread.getUser());
                existing.setTitle(thread.getTitle());
                existing.setDate(thread.getDate());
            }
        } catch (Exception e) {
            System.out.println("Error updating thread: " + e.getMessage());
        }
    }
    
    @Transactional
    public void delete(Threads thread) {
        try {
            Threads entity = Threads.findById(thread.getThreadId());
            if (entity != null) {
                entity.delete();
            }
        } catch (Exception e) {
            System.out.println("Error deleting thread: " + e.getMessage());
        }
    }
    
    public List<Threads> listAll() {
        return Threads.listAll();
    }
    
    public List<Threads> findByUser(Users user) {
        return Threads.list("user", user);
    }
    
    public Threads findById(Long id) {
        return Threads.findById(id);
    }
    
    public long count() {
        return Threads.count();
    }
}