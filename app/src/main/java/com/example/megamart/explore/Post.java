package com.example.megamart.explore;

import java.util.List;

public class Post {
    private int id;
    private String username;
    private String post_content;
    private String post_image;
    private int like_count;
    private boolean isLiked;
    private List<Comment> comments; // Add this field

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPost_content() { return post_content; }
    public String getPost_image() { return post_image; }
    public int getLike_count() { return like_count; }
    public boolean isLiked() { return isLiked; }
    public List<Comment> getComments() { return comments; }

    // Setters
    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }
    public void setLiked(boolean liked) {
        isLiked = liked;
    }
    public void setComments(List<Comment> comments) { // New setter
        this.comments = comments;
    }
}