package com.example.megamart.explore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megamart.R;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postlayout, parent, false); // Use post_layout.xml
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textView.setText(post.getPost_content());
        holder.username.setText(post.getUsername());

        // Set like count
        holder.likeCountText.setText(post.getLike_count() + " Likes");

        // Decode base64 image
        try {
            byte[] decodedBytes = Base64.decode(post.getPost_image(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace(); // Log decoding errors
        }

        // Handle like button click
        holder.likeButton.setOnClickListener(v -> {
            // Increment like count and update UI
            int newLikeCount = post.getLike_count() + 1;
            post.setLike_count(newLikeCount);
            holder.likeCountText.setText(newLikeCount + " Likes");

            // Update like count in the database
            updateLikeCountInDatabase(post.getId(), newLikeCount);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    private void updateLikeCountInDatabase(int postId, int newLikeCount) {
        // Async task to update the like count in the database
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.150.81:7070/event_api/update_likes.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                String data = "post_id=" + postId + "&like_count=" + newLikeCount;
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Success, handle response if needed
                } else {
                    // Error updating like count
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, username, likeCountText;
        ImageView likeButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagePost);
            textView = itemView.findViewById(R.id.textPost);
            username = itemView.findViewById(R.id.postusername);
            likeCountText = itemView.findViewById(R.id.like_count);
            likeButton = itemView.findViewById(R.id.like_button);
        }
    }
}
