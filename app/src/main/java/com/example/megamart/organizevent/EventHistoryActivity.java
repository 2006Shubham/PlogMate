package com.example.megamart.organizevent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megamart.R;
import com.example.megamart.organizevent.adapters.EventAdapter;
import com.example.megamart.organizevent.datamodels.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private ActivityResultLauncher<Intent> editEventLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);

        recyclerView = findViewById(R.id.recyclerViewEventHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();

        // Register the launcher
        editEventLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Event updatedEvent = (Event) result.getData().getSerializableExtra("updated_event");
                        int position = result.getData().getIntExtra("position", -1);
                        if (updatedEvent != null && position != -1) {
                            eventList.set(position, updatedEvent);
                            eventAdapter.notifyItemChanged(position);
                        }
                    }
                }
        );

        new FetchEventHistoryTask().execute();
    }

    private class FetchEventHistoryTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL("http://192.168.150.81:7070/event_api/fetch_events.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                int data = reader.read();
                while (data != -1) {
                    result.append((char) data);
                    data = reader.read();
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(EventHistoryActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonResponse = new JSONObject(result);
                JSONArray eventArray = jsonResponse.getJSONArray("events");

                for (int i = 0; i < eventArray.length(); i++) {
                    JSONObject eventObject = eventArray.getJSONObject(i);
                    Event event = new Event(
                            eventObject.getString("event_name"),
                            eventObject.getString("event_date"),
                            eventObject.getString("event_address"),
                            eventObject.getString("event_description"),
                            Integer.parseInt(eventObject.getString("event_id"))
                    );
                    eventList.add(event);
                }

                eventAdapter = new EventAdapter(eventList, thisActivity(), editEventLauncher);
                recyclerView.setAdapter(eventAdapter);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(EventHistoryActivity.this, "Error parsing event data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private EventHistoryActivity thisActivity() {
        return this;
    }
}
