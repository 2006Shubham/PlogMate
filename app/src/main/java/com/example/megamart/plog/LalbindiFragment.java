package com.example.megamart.plog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.megamart.R;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LalbindiFragment extends Fragment {


    private RecyclerView recyclerView;
    private List<Event> eventList;
    private EventAdapter adapter;

    public LalbindiFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plogging, container, false);
        recyclerView = view.findViewById(R.id.plogRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventList = new ArrayList<>();
        adapter = new EventAdapter(getContext(), eventList,new LalbindiFragment());
        recyclerView.setAdapter(adapter);

        lalBindiEvents();
        return view;
    }

    private void lalBindiEvents() {
        String url = "http://192.168.150.81:7070/event_api/get_events.php?event_type=LB";

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Event event = new Event(
                                    obj.getString("event_name"),
                                    obj.getString("event_date"),
                                    obj.getString("event_address"),
                                    obj.getString("event_time"),
                                    obj.getString("map_link"),
                                    obj.getString("coordinator_name"),
                                    obj.getString("mobile_number"),
                                    obj.getString("event_description"),
                                    obj.getString("event_image"));
                            eventList.add(event);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getContext(), "Failed to fetch events", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}