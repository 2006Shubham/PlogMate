package com.example.megamart.editevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.megamart.R;
import com.example.megamart.organizevent.AddEventActivity;
import com.example.megamart.organizevent.datamodels.Event;

import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {

    EditText nameEdit, dateEdit, locationEdit, descriptionEdit,timeEdit,mapEdit,cordinatorEdit,mobEdit;
    Button editButton, saveButton;
    RadioButton pl,lb,ss;


    Event event;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event2);

        // Receive event data
        event = (Event) getIntent().getSerializableExtra("event");
        position = getIntent().getIntExtra("position", -1);

        // Initialize views
        nameEdit = findViewById(R.id.etAddEventname);
        dateEdit = findViewById(R.id.etAddEventDate);
        locationEdit = findViewById(R.id.etAddEventAddress);
        descriptionEdit = findViewById(R.id.etAddEventDescription);
        timeEdit = findViewById(R.id.etAddEventTime);
        mapEdit = findViewById(R.id.etAddEventMapLink);
        cordinatorEdit = findViewById(R.id.etCordinatiorName);
        mobEdit = findViewById(R.id.etAddEventMobNo);
        editButton = findViewById(R.id.editButton);
        saveButton = findViewById(R.id.saveButton);

        // Set initial values
        nameEdit.setText(event.getName());
        dateEdit.setText(event.getDate());
        locationEdit.setText(event.getLocation());
        descriptionEdit.setText(event.getDescription());



        dateEdit.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditEventActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        dateEdit.setText(formattedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Time picker
        timeEdit.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    EditEventActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        timeEdit.setText(formattedTime);
                    },
                    hour, minute, true);
            timePickerDialog.show();
        });




        pl = findViewById(R.id.radioPlogging);
        lb = findViewById(R.id.radioLalBindi);
        ss = findViewById(R.id.radioSocialShelf);


        // Enable editing on button click
        editButton.setOnClickListener(v -> setEditable(true));

        // Save updated event
        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void setEditable(boolean editable) {
        nameEdit.setFocusable(editable);
        nameEdit.setFocusableInTouchMode(editable);
        nameEdit.setClickable(editable);

        dateEdit.setFocusable(editable);
        dateEdit.setFocusableInTouchMode(editable);
        dateEdit.setClickable(editable);

        locationEdit.setFocusable(editable);
        locationEdit.setFocusableInTouchMode(editable);
        locationEdit.setClickable(editable);

        timeEdit.setFocusable(editable);
        timeEdit.setFocusableInTouchMode(editable);
        timeEdit.setClickable(editable);

        mapEdit.setFocusable(editable);
        mapEdit.setFocusableInTouchMode(editable);
        mapEdit.setClickable(editable);

        nameEdit.setFocusable(editable);
        nameEdit.setFocusableInTouchMode(editable);
        nameEdit.setClickable(editable);

        mobEdit.setFocusable(editable);
        mobEdit.setFocusableInTouchMode(editable);
        mobEdit.setClickable(editable);

        descriptionEdit.setFocusable(editable);
        descriptionEdit.setFocusableInTouchMode(editable);
        descriptionEdit.setClickable(editable);

        pl.setEnabled(editable);
        lb.setEnabled(editable);
        ss.setEnabled(editable);

    }

    private void saveChanges() {
        String name = nameEdit.getText().toString().trim();
        String date = dateEdit.getText().toString().trim();
        String location = locationEdit.getText().toString().trim();
        String description = descriptionEdit.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construct URL with encoded parameters
        String url = "http://192.168.150.81:7070/event_api/updateEvent.php?id=" + event.getId()
                + "&name=" + Uri.encode(name)
                + "&date=" + Uri.encode(date)
                + "&location=" + Uri.encode(location)
                + "&description=" + Uri.encode(description);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Toast.makeText(EditEventActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                    // Return updated event
                    Intent resultIntent = new Intent();
                    Event updatedEvent = new Event(name, date, location, description, event.getId());
                    resultIntent.putExtra("updated_event", updatedEvent);
                    resultIntent.putExtra("position", position);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                },
                error -> {
                    Toast.makeText(EditEventActivity.this, "Update failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        queue.add(request);
    }
}
