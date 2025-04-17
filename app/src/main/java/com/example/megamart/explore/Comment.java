package com.example.megamart.explore;

public class Comment {
    private int id;
    private int post_id;
    private String username;
    private String comment_text;
    private String created_at;

    // Getters
    public int getId() { return id; }
    public int getPost_id() { return post_id; }
    public String getUsername() { return username; }
    public String getComment_text() { return comment_text; }
    public String getCreated_at() { return created_at; }
}