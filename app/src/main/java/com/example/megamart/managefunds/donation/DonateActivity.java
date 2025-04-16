package com.example.megamart.managefunds.donation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megamart.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DonateActivity extends AppCompatActivity {

    EditText etDonorName, etAmount, etMobileNumber;
    Button btnSaveDonor;
    RecyclerView rvDonators;

    DonorAdapter adapter;
    ArrayList<Donor> donorList = new ArrayList<>();

    String insertUrl = "http://192.168.150.81:7070/event_api/insert_donation.php";
    String fetchUrl = "http://192.168.150.81:7070/event_api/get_donors.php"; // You must create this PHP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        etDonorName = findViewById(R.id.etDonorName);
        etAmount = findViewById(R.id.etAmount);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        btnSaveDonor = findViewById(R.id.btnSaveDonor);
        rvDonators = findViewById(R.id.rvDonators);

        rvDonators.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DonorAdapter(donorList);
        rvDonators.setAdapter(adapter);

        btnSaveDonor.setOnClickListener(view -> saveDonor());

        fetchDonors(); // Fetch existing donors on load
    }

    private void saveDonor() {
        String name = etDonorName.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String mobile = etMobileNumber.getText().toString().trim();

        if (name.isEmpty() || amount.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String requestUrl = insertUrl + "?donor_name=" + name + "&amount=" + amount + "&mobile=" + mobile;

        new Thread(() -> {
            try {
                URL url = new URL(requestUrl.replace(" ", "%20"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                runOnUiThread(() -> {
                    if (responseCode == 200) {
                        Toast.makeText(this, "Donor Saved Successfully", Toast.LENGTH_SHORT).show();
                        etDonorName.setText("");
                        etAmount.setText("");
                        etMobileNumber.setText("");
                        fetchDonors(); // Refresh list
                    } else {
                        Toast.makeText(this, "Failed to save donor", Toast.LENGTH_SHORT).show();
                    }
                });

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void fetchDonors() {
        new Thread(() -> {
            try {
                URL url = new URL(fetchUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                conn.disconnect();

                JSONArray jsonArray = new JSONArray(response.toString());
                donorList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Donor donor = new Donor(
                            obj.getString("donor_name"),
                            obj.getString("amount"),
                            obj.getString("mobile")
                    );
                    donorList.add(donor);
                }

                runOnUiThread(() -> adapter.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error fetching donors", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
