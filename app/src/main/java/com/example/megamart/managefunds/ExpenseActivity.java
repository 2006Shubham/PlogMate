package com.example.megamart.managefunds;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.megamart.R;
import com.example.megamart.managefunds.expense.SelectEventActivity;
import com.example.megamart.managefunds.expense.TotalExpenseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {

    EditText etEventName;
    String selectedEventId;

    Button save;
    RecyclerView rvExpenseList;
    EventAdapter eventAdapter;
    List<Event> eventList = new ArrayList<>();

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String selectedEventName = result.getData().getStringExtra("selected_event_name");
                    selectedEventId = result.getData().getStringExtra("event_id");
                    etEventName.setText(selectedEventName);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEventName = findViewById(R.id.etEventName);
        save = findViewById(R.id.btnSaveExpense);
        rvExpenseList = findViewById(R.id.rvExpenseList);

        etEventName.setFocusable(false); // prevent keyboard
        etEventName.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseActivity.this, SelectEventActivity.class);
            launcher.launch(intent);
        });

        save.setOnClickListener(v -> saveEventWithExpenses());

        // Set up RecyclerView
        rvExpenseList.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList);
        rvExpenseList.setAdapter(eventAdapter);

        // Fetch events from the server
        fetchEvents();
    }

    private void fetchEvents() {
        String url = "http://192.168.150.81:7070/event_api/get_events_expenses_recycler.php";

        // Make request using Volley
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Parse the response
                        JSONArray eventsArray = new JSONArray(response);
                        eventList.clear();
                        for (int i = 0; i < eventsArray.length(); i++) {
                            JSONObject eventObj = eventsArray.getJSONObject(i);
                            String eventId = eventObj.getString("event_id");
                            String eventName = eventObj.getString("event_name");

                            // Add event to list
                            eventList.add(new Event(eventId, eventName));
                        }

                        // Notify adapter of data changes
                        eventAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error fetching events", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to fetch events", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void saveEventWithExpenses() {
        String expenseName = ((EditText) findViewById(R.id.etExpenseName)).getText().toString().trim();
        String amountStr = ((EditText) findViewById(R.id.etAmount)).getText().toString().trim();

        if (selectedEventId == null || selectedEventId.isEmpty()) {
            Toast.makeText(this, "Please select an event first.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (expenseName.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter both expense name and amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.150.81:7070/event_api/save_expense.php" +
                "?event_id=" + selectedEventId +
                "&expense_name=" + expenseName +
                "&amount=" + amountStr;

        // Make request using Volley
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show();
                    fetchEvents();
                },
                error -> {
                    Toast.makeText(this, "Failed to save expense", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        Volley.newRequestQueue(this).add(request);
    }

    // Event class
    public static class Event {
        String eventId, eventName;

        public Event(String id, String name) {
            this.eventId = id;
            this.eventName = name;
        }
    }

    // EventAdapter for RecyclerView
    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
        List<Event> list;

        public EventAdapter(List<Event> list) {
            this.list = list;
        }

        class EventViewHolder extends RecyclerView.ViewHolder {
            TextView tvEventName;

            public EventViewHolder(View itemView) {
                super(itemView);
                tvEventName = itemView.findViewById(android.R.id.text1); // Simple text view
            }
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            Event e = list.get(position);
            holder.tvEventName.setText(e.eventName);

            holder.tvEventName.setOnClickListener(v -> {
                Intent intent = new Intent(ExpenseActivity.this, TotalExpenseActivity.class);
                intent.putExtra("event_id", e.eventId);
                intent.putExtra("event_name", e.eventName);
                startActivity(intent);
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
