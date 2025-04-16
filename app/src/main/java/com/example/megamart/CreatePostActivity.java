package com.example.megamart;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreatePostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;

    private EditText postEditText;
    private ImageView postImageView;
    private LinearLayout btnSelectImage;
    private String encodedImage = "";

    private  TextView tvusername    ;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createpost_layout);

        postEditText = findViewById(R.id.post);
        postImageView = findViewById(R.id.ivEventImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        Button saveButton = findViewById(R.id.saveButton);
        //saveButton.setOnClickListener(v -> new UploadPostTask().execute());

        // inside onClick of saveButton
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postText = postEditText.getText().toString().trim();

                if (postText.isEmpty()) {
                    Toast.makeText(CreatePostActivity.this, "Please write the post", Toast.LENGTH_LONG).show();
                    return;
                }

                if (encodedImage.isEmpty()) {
                    Toast.makeText(CreatePostActivity.this, "Please select an image", Toast.LENGTH_LONG).show();
                    return;
                }

                // All fields valid, now execute the upload task
                new UploadPostTask().execute();

            }
        });


        SharedPreferences prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = prefs.getString("username","user");

        tvusername =  findViewById(R.id.tvUsername);

        tvusername.setText(username);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                postImageView.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class UploadPostTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String postText = postEditText.getText().toString();

                String postData = "post_content=" + Uri.encode(postText) +
                        "&post_image=" + Uri.encode(encodedImage) +
                        "&username=" + Uri.encode(username);


                URL url = new URL("http://192.168.150.81:7070/event_api/insert_post.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.getOutputStream().write(postData.getBytes());

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("UploadResult", result);
            Toast.makeText(CreatePostActivity.this, result, Toast.LENGTH_LONG).show();
            finish();

        }
    }
}
