package com.example.megamart.managefunds.collab;

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

public class CollabActivity extends AppCompatActivity {

    EditText etCollaboratorName, etOwner, etPurpose;
    Button btnSaveCollaborator;
    RecyclerView rvCollaborators;
    CollaboratorAdapter adapter;
    ArrayList<Collaborator> collaboratorList = new ArrayList<>();

    String insertUrl = "http://192.168.150.81:7070/event_api/insert_collaborator.php";
    String fetchUrl = "http://192.168.150.81:7070/event_api/get_collaborators.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab);

        etCollaboratorName = findViewById(R.id.etCollaboratorName);
        etOwner = findViewById(R.id.etOwner);
        etPurpose = findViewById(R.id.etPurposeofCollab);
        btnSaveCollaborator = findViewById(R.id.btnSaveExpense);
        rvCollaborators = findViewById(R.id.rvCollaborators);

        rvCollaborators.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CollaboratorAdapter(collaboratorList);
        rvCollaborators.setAdapter(adapter);

        btnSaveCollaborator.setOnClickListener(view -> saveCollaborator());

        fetchCollaborators(); // Fetch existing collaborators on load
    }

    private void saveCollaborator() {
        String collaboratorName = etCollaboratorName.getText().toString().trim();
        String owner = etOwner.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();

        if (collaboratorName.isEmpty() || owner.isEmpty() || purpose.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String requestUrl = insertUrl + "?collaborator_name=" + collaboratorName + "&owner=" + owner + "&purpose=" + purpose;

        new Thread(() -> {
            try {
                URL url = new URL(requestUrl.replace(" ", "%20"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                runOnUiThread(() -> {
                    if (responseCode == 200) {
                        Toast.makeText(this, "Collaboration Saved Successfully", Toast.LENGTH_SHORT).show();
                        etCollaboratorName.setText("");
                        etOwner.setText("");
                        etPurpose.setText("");
                        fetchCollaborators(); // Refresh list
                    } else {
                        Toast.makeText(this, "Failed to save collaboration", Toast.LENGTH_SHORT).show();
                    }
                });

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void fetchCollaborators() {
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
                collaboratorList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Collaborator collaborator = new Collaborator(
                            obj.getString("collaborator_name"),
                            obj.getString("owner"),
                            obj.getString("purpose")
                    );
                    collaboratorList.add(collaborator);
                }

                runOnUiThread(() -> adapter.notifyDataSetChanged());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error fetching collaborators", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
