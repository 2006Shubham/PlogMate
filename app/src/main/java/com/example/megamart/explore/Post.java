package com.example.megamart.explore;

public class Post {
    private int id;
    private String username;
    private String post_content;
    private String post_image;
    private int like_count;

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPost_content() { return post_content; }
    public String getPost_image() { return post_image; }
    public int getLike_count() { return like_count; }

    // Setter for updating like count locally
    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }
}
