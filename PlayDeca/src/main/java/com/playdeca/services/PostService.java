package com.playdeca.services;

import com.playdeca.models.Posts;
import com.playdeca.models.PostVotes;
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
        return Posts.<Posts>listAll();
    }
    
    public List<Posts> findByThread(Threads thread) {
        return Posts.<Posts>list("thread = ?1 ORDER BY date DESC", thread);
    }
    
    public List<Posts> findByUser(Users user) {
        return Posts.<Posts>list("user", user);
    }
    
    public Posts findById(Long id) {
        return Posts.findById(id);
    }
    
    public long count() {
        return Posts.<Posts>count();
    }
    
    @Transactional
    public void upvotePost(Posts post, Users user) {
        try {
            PostVotes existingVote = PostVotes.<PostVotes>find("post = ?1 and user = ?2", post, user).firstResult();
            if (existingVote != null) {
                if (existingVote.getVoteType() == 1) {
                    existingVote.delete();
                } else {
                    existingVote.setVoteType(1);
                    existingVote.setTimestamp(LocalDateTime.now());
                }
            } else {
                PostVotes vote = new PostVotes(post, user, 1);
                vote.persist();
            }
            recalculateVotes(post);
        } catch (Exception e) {
            System.out.println("Error upvoting post: " + e.getMessage());
        }
    }
    
    @Transactional
    public void downvotePost(Posts post, Users user) {
        try {
            PostVotes existingVote = PostVotes.<PostVotes>find("post = ?1 and user = ?2", post, user).firstResult();
            if (existingVote != null) {
                if (existingVote.getVoteType() == -1) {
                    existingVote.delete();
                } else {
                    existingVote.setVoteType(-1);
                    existingVote.setTimestamp(LocalDateTime.now());
                }
            } else {
                PostVotes vote = new PostVotes(post, user, -1);
                vote.persist();
            }
            recalculateVotes(post);
        } catch (Exception e) {
            System.out.println("Error downvoting post: " + e.getMessage());
        }
    }
    
    @Transactional
    public void removeVote(Posts post, Users user) {
        try {
            PostVotes existingVote = PostVotes.<PostVotes>find("post = ?1 and user = ?2", post, user).firstResult();
            if (existingVote != null) {
                existingVote.delete();
                recalculateVotes(post);
            }
        } catch (Exception e) {
            System.out.println("Error removing vote: " + e.getMessage());
        }
    }
    
    public int getUserVote(Posts post, Users user) {
        try {
            PostVotes existingVote = PostVotes.<PostVotes>find("post = ?1 and user = ?2", post, user).firstResult();
            if (existingVote != null) {
                return existingVote.getVoteType();
            }
        } catch (Exception e) {
            System.out.println("Error getting user vote: " + e.getMessage());
        }
        return 0;
    }
    
    @Transactional
    public void recalculateVotes(Posts post) {
        try {
            long upvotes = PostVotes.<PostVotes>count("post = ?1 and voteType = 1", post);
            long downvotes = PostVotes.<PostVotes>count("post = ?1 and voteType = -1", post);
            post.setUpvotes((int) upvotes);
            post.setDownvotes((int) downvotes);
            post.persist();
        } catch (Exception e) {
            System.out.println("Error recalculating votes: " + e.getMessage());
        }
    }
    
    public long getPostCountByThread(Threads thread) {
        return Posts.<Posts>count("thread", thread);
    }
}