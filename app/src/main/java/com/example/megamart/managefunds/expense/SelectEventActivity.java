package com.example.megamart.managefunds.expense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.megamart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectEventActivity extends AppCompatActivity {
    RecyclerView rvFilteredEvents;
    RadioGroup radioGroupEventType;
    String selectedType = "Plogging"; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event);

        rvFilteredEvents = findViewById(R.id.rvFilteredEvents);
        radioGroupEventType = findViewById(R.id.radioGroupEventType);

        radioGroupEventType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPlogging) selectedType = "PL";
            else if (checkedId == R.id.rbLalBindi) selectedType = "LB";
            else if (checkedId == R.id.rbSocialShelf) selectedType = "SS";

            loadEvents(selectedType);
        });

        loadEvents(selectedType); // initial load
    }

    private void loadEvents(String eventType) {
        String url = "http://192.168.150.81:7070/event_api/expense_get_events.php?event_type=" + eventType;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                List<EventModel> list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    EventModel model = new EventModel();
                    model.setEvent_id(obj.getString("event_id"));
                    model.setEvent_name(obj.getString("event_name"));
                    list.add(model);
                }

                EventAdapter adapter = new EventAdapter(SelectEventActivity.this, list, model -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selected_event_name", model.getEvent_name());
                    resultIntent.putExtra("event_id",model.getEvent_id());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                });

                rvFilteredEvents.setLayoutManager(new LinearLayoutManager(this));
                rvFilteredEvents.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "JSON Error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        });

        queue.add(request);
    }





}
