package com.playdeca.services;

import com.playdeca.models.Posts;
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
public class PostService {
    
    @Transactional
    public void create(Posts post) {
        try {
            post.setDate(LocalDateTime.now());
            post.setUpvotes(0);
            post.setDownvotes(0);
            post.persist();
        } catch (Exception e) {
            System.out.println("Error creating post: " + e.getMessage());
        }
    }
    
    @Transactional
    public void update(Posts post) {
        try {
            Posts existing = Posts.findById(post.getPostId());
            if (existing != null) {
                existing.setThread(post.getThread());
                existing.setUser(post.getUser());
                existing.setTitle(post.getTitle());
                existing.setBody(post.getBody());
                existing.setUpvotes(post.getUpvotes());
                existing.setDownvotes(post.getDownvotes());
                existing.setDate(post.getDate());
            }
        } catch (Exception e) {
            System.out.println("Error updating post: " + e.getMessage());
        }
    }
    
    @Transactional
    public void delete(Posts post) {
        try {
            Posts entity = Posts.findById(post.getPostId());
            if (entity != null) {
                entity.delete();
            }
        } catch (Exception e) {
            System.out.println("Error deleting post: " + e.getMessage());
        }
    }
    
    public List<Posts> listAll() {
        return Posts.listAll();
    }
    
    public List<Posts> findByThread(Threads thread) {
        return Posts.list("thread", thread);
    }
    
    public List<Posts> findByUser(Users user) {
        return Posts.list("user", user);
    }
    
    public Posts findById(Long id) {
        return Posts.findById(id);
    }
    
    public long count() {
        return Posts.count();
    }
}