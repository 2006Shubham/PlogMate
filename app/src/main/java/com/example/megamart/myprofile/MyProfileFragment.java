package com.example.megamart.myprofile;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import com.example.megamart.R;
import com.example.megamart.explore.Post;
import com.example.megamart.explore.PostAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;


public class MyProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList = new ArrayList<>();
    private String username;

    TextView postsCountTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        // Get username from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("loginPrefs", getContext().MODE_PRIVATE);
        username = prefs.getString("username", "Guest");

        TextView usernameTextView = view.findViewById(R.id.username);
        usernameTextView.setText(username);

        recyclerView = view.findViewById(R.id.recyclerUserPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);
        postsCountTextView = view.findViewById(R.id.posts_count);
        new FetchUserPostsTask().execute();

        return view;
    }

    private class FetchUserPostsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String urlString = "http://192.168.150.81:7070/event_api/get_user_posts.php?username=" + URLEncoder.encode(username, "UTF-8");
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }


        @Override
        protected void onPostExecute(String result) {
            try {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Post>>() {}.getType();
                postList.clear();
                postList.addAll(gson.fromJson(result, listType));
                adapter.notifyDataSetChanged();

                // ✅ Set post count

                if (postsCountTextView != null) {
                    postsCountTextView.setText(postList.size() + " Posts");
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error loading user posts", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
