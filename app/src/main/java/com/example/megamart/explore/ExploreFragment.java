package com.example.megamart.explore;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.megamart.CreatePostActivity;
import com.example.megamart.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.*;

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter adapter;

    LinearLayout writepost;
    private List<Post> postList = new ArrayList<>();

    @Override
    public void  onResume() {
        super.onResume();
        new FetchPostsTask().execute();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerView = view.findViewById(R.id.recyclerPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // In ExploreFragment.java
        adapter = new PostAdapter(postList, requireContext());
        recyclerView.setAdapter(adapter);

        writepost = view.findViewById(R.id.writepost);

        writepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreatePostActivity.class);
                startActivity(intent);

            }
        });

        new FetchPostsTask().execute();

        return view;
    }

    private class FetchPostsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.150.81:7070/event_api/get_posts.php");
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
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error loading posts", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
