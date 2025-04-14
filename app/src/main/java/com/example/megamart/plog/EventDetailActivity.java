package com.example.megamart.plog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.megamart.R;

public class EventDetailActivity extends AppCompatActivity {

    TextView tvEventName, tvEventDate, tvEventTime, tvAddress, tvMapLink, tvCoordinator, tvMobile, tvDescription;
    ImageView eventImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Bind views
        tvEventName = findViewById(R.id.tvEventName);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventTime = findViewById(R.id.tvEventTime);
        tvAddress = findViewById(R.id.tvAddress);
        tvMapLink = findViewById(R.id.tvMapLink);
        tvCoordinator = findViewById(R.id.tvCoordinator);
        tvMobile = findViewById(R.id.tvMobile);
        tvDescription = findViewById(R.id.tvDescription);

        eventImage = findViewById(R.id.event_image);

        // Receive event data
        Event event = (Event) getIntent().getSerializableExtra("event_data");

        if (event != null) {
            tvEventName.setText(event.getEvent_name());
            tvEventDate.setText("Date: " + event.getEvent_date());
            tvEventTime.setText("Time: " + event.getEvent_time());
            tvAddress.setText("Address: " + event.getEvent_address());
            tvMapLink.setText("Map: " + event.getMap_link());
            tvCoordinator.setText("Coordinator: " + event.getCoordinator_name());
            tvMobile.setText("Mobile: " + event.getMobile_number());
            tvDescription.setText("Description: " + event.getEvent_description());


            String encodedImage = event.getEvent_image();  // Get the Base64 image string
            if (encodedImage != null && !encodedImage.isEmpty()) {
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                eventImage.setImageBitmap(decodedBitmap);  // Set decoded image to ImageView
            } else {
                eventImage.setImageResource(R.drawable.drive);  // Fallback image if no image is set
            }



        }
    }
}
