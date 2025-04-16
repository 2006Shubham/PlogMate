package com.example.megamart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.megamart.organizevent.AddEventActivity;
import com.example.megamart.organizevent.EventHistoryActivity;

public class OrganizeEventActivity extends AppCompatActivity {


    CardView addevent,updateevent,eventhistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organize_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        addevent = findViewById(R.id.cardAddEvent);
        updateevent = findViewById(R.id.cardUpdateEvent);
        eventhistory = findViewById(R.id.cardEventHistory);

        addevent.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(OrganizeEventActivity.this, AddEventActivity.class);
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });



        eventhistory.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(OrganizeEventActivity.this, EventHistoryActivity.class);
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });



    }
}