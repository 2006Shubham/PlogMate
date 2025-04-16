package com.example.megamart.organizevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.megamart.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDate, etEventAddress, etEventTime, etMapLink, etCoordinatorName, etMobileNo, etEventDescription;
    private Button btnSave, btnSelectImage;
    private ImageView ivEventImage;
    private RadioGroup radioGroup;
    private String encodedImage = "";

    private static final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Initialize views
        etEventName = findViewById(R.id.etAddEventname);
        etEventDate = findViewById(R.id.etAddEventDate);
        etEventAddress = findViewById(R.id.etAddEventAddress);
        etEventTime = findViewById(R.id.etAddEventTime);
        etMapLink = findViewById(R.id.etAddEventMapLink);
        etCoordinatorName = findViewById(R.id.etCordinatiorName);
        etMobileNo = findViewById(R.id.etAddEventMobNo);
        etEventDescription = findViewById(R.id.etAddEventDescription);
        btnSave = findViewById(R.id.saveButton);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivEventImage = findViewById(R.id.ivEventImage);
        radioGroup = findViewById(R.id.radioGroupEventType);

        // Date picker
        etEventDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
                    etEventDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time picker
        etEventTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) ->
                    etEventTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

        // Select Image
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Save
        btnSave.setOnClickListener(v -> new SaveEventTask().execute());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ivEventImage.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SaveEventTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String eventName = etEventName.getText().toString();
                String eventDate = etEventDate.getText().toString();
                String eventAddress = etEventAddress.getText().toString();
                String eventTime = etEventTime.getText().toString();
                String mapLink = etMapLink.getText().toString();
                String coordinatorName = etCoordinatorName.getText().toString();
                String mobileNo = etMobileNo.getText().toString();
                String eventDescription = etEventDescription.getText().toString();
                String eventType = getEventType(); // Get event type (PL, LB, SS)

                // Construct the POST data
                String postData = "event_name=" + Uri.encode(eventName) +
                        "&event_date=" + Uri.encode(eventDate) +
                        "&event_address=" + Uri.encode(eventAddress) +
                        "&event_time=" + Uri.encode(eventTime) +
                        "&map_link=" + Uri.encode(mapLink) +
                        "&coordinator_name=" + Uri.encode(coordinatorName) +
                        "&mobile_number=" + Uri.encode(mobileNo) +
                        "&event_description=" + Uri.encode(eventDescription) +
                        "&event_type=" + Uri.encode(eventType) +
                        "&event_image=" + Uri.encode(encodedImage);

                // URL to your PHP backend

                URL url = new URL("http://192.168.150.81:7070/event_api/insert_event.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.getOutputStream().write(postData.getBytes());

                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d("Response", result);
                Toast.makeText(AddEventActivity.this, "Event Saved!", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Error", "Failed to save event");
                Toast.makeText(AddEventActivity.this, "Failed to Save", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getEventType() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radioPlogging) return "PL";
        else if (selectedId == R.id.radioLalBindi) return "LB";
        else if (selectedId == R.id.radioSocialShelf) return "SS";
        return "";
    }
}
