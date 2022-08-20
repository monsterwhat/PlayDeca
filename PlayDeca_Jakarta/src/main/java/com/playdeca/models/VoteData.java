package com.playdeca.models;

public class VoteData {
    
    private ForumData forum_id;
    private boolean voted_up;
    private boolean voted_down;
    private UserData user_id;

    public ForumData getForum_id() {
        return forum_id;
    }

    public void setForum_id(ForumData forum_id) {
        this.forum_id = forum_id;
    }

    public boolean isVoted_up() {
        return voted_up;
    }

    public void setVoted_up(boolean voted_up) {
        this.voted_up = voted_up;
    }

    public boolean isVoted_down() {
        return voted_down;
    }

    public void setVoted_down(boolean voted_down) {
        this.voted_down = voted_down;
    }

    public UserData getUser_id() {
        return user_id;
    }

    public void setUser_id(UserData user_id) {
        this.user_id = user_id;
    }
    
}
