package com.example.megamart.explore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.megamart.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList;
    private final Context context;
    private String currentUsername;


    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;

        // Get current username from SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        currentUsername = prefs.getString("username", "Guest");
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postlayout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textView.setText(post.getPost_content());
        holder.username.setText(post.getUsername());
        holder.likeCountText.setText(post.getLike_count() + " Likes");

        // Set like button icon
        holder.likeButton.setImageResource(
                post.isLiked() ? R.drawable.like : R.drawable.like
        );

        // Decode base64 image
        try {
            byte[] decodedBytes = Base64.decode(post.getPost_image(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Like button click
        holder.likeButton.setOnClickListener(v -> {
            int newLikeCount = post.isLiked() ? post.getLike_count() - 1 : post.getLike_count() + 1;
            boolean newLikedStatus = !post.isLiked();

            post.setLike_count(newLikeCount);
            post.setLiked(newLikedStatus);

            holder.likeCountText.setText(newLikeCount + " Likes");
            holder.likeButton.setImageResource(
                    newLikedStatus ? R.drawable.like : R.drawable.like
            );

            updateLikeStatusInDatabase(post.getId(), newLikeCount, newLikedStatus);
        });

        // Comment button click - toggle comments section
        holder.commentButton.setOnClickListener(v -> {
            boolean isVisible = holder.commentsSection.getVisibility() == View.VISIBLE;
            holder.commentsSection.setVisibility(isVisible ? View.GONE : View.VISIBLE);

            if (!isVisible && (post.getComments() == null || post.getComments().isEmpty())) {
                fetchCommentsForPost(post.getId(), holder.commentsContainer);
            } else if (!isVisible) {
                displayComments(post.getComments(), holder.commentsContainer);
            }
        });

        // Post comment button click
        holder.postCommentButton.setOnClickListener(v -> {
            String commentText = holder.commentInput.getText().toString().trim();
            if (!commentText.isEmpty()) {
                postComment(post.getId(), currentUsername, commentText, holder.commentsContainer);
                holder.commentInput.setText("");
            }
        });
    }

    private void updateLikeStatusInDatabase(int postId, int newLikeCount, boolean isLiked) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.150.81:7070/event_api/update_likes.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                // Include liked status in the request
                String data = "post_id=" + postId + "&like_count=" + newLikeCount + "&is_liked=" + (isLiked ? "1" : "0");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    // Handle error
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fetchCommentsForPost(int postId, LinearLayout commentsContainer) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.150.81:7070/event_api/get_comments.php?post_id=" + postId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Comment>>() {}.getType();
                List<Comment> comments = gson.fromJson(result.toString(), listType);

                // Update UI on main thread
                ((Activity) context).runOnUiThread(() -> displayComments(comments, commentsContainer));

                // Update the post in the list
                for (Post post : postList) {
                    if (post.getId() == postId) {
                        post.setComments(comments);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void displayComments(List<Comment> comments, LinearLayout container) {
        container.removeAllViews();

        if (comments == null || comments.isEmpty()) {
            TextView noComments = new TextView(context);
            noComments.setText("No comments yet");
            noComments.setTextSize(12);
            container.addView(noComments);
            return;
        }

        for (Comment comment : comments) {
            View commentView = LayoutInflater.from(context).inflate(R.layout.comment_item, container, false);

            TextView username = commentView.findViewById(R.id.comment_username);
            TextView text = commentView.findViewById(R.id.comment_text);

            username.setText(comment.getUsername());
            text.setText(comment.getComment_text());

            container.addView(commentView);
        }
    }

    private void postComment(int postId, String username, String commentText, LinearLayout commentsContainer) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.150.81:7070/event_api/add_comment.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                String data = "post_id=" + postId +
                        "&username=" + URLEncoder.encode(username, "UTF-8") +
                        "&comment_text=" + URLEncoder.encode(commentText, "UTF-8");

                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                // Parse response
                Gson gson = new Gson();
                Map<String, Object> response = gson.fromJson(result.toString(),
                        new TypeToken<Map<String, Object>>() {}.getType());

                if ("success".equals(response.get("status"))) {
                    // Refresh comments after successful post
                    ((Activity) context).runOnUiThread(() -> fetchCommentsForPost(postId, commentsContainer));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, username, likeCountText;
        ImageView likeButton, commentButton;
        LinearLayout commentsSection, commentsContainer;
        EditText commentInput;
        Button postCommentButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagePost);
            textView = itemView.findViewById(R.id.textPost);
            username = itemView.findViewById(R.id.postusername);
            likeCountText = itemView.findViewById(R.id.like_count);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);

            commentsSection = itemView.findViewById(R.id.comments_section);
            commentsContainer = itemView.findViewById(R.id.comments_container);
            commentInput = itemView.findViewById(R.id.comment_input);
            postCommentButton = itemView.findViewById(R.id.post_comment_button);
        }
    }
}