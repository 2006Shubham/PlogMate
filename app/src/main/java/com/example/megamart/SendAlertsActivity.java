package com.example.megamart;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SendAlertsActivity extends AppCompatActivity {

    EditText editMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_alerts);

        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendNotificationToAllUsers(message);
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotificationToAllUsers(String message) {
        try {
            String encodedMessage = URLEncoder.encode(message, "UTF-8");
            String url = "http://192.168.150.81:7070/event_api/send_notification.php?message=" + encodedMessage;

            Log.d("FCM@", url);

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> Toast.makeText(this, "Notification Sent!", Toast.LENGTH_SHORT).show(),
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Failed: " + error.toString(), Toast.LENGTH_LONG).show();
                    });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Encoding Error", Toast.LENGTH_SHORT).show();
        }
    }

}
