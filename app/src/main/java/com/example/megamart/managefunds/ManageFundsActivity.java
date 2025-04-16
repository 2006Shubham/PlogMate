package com.example.megamart.managefunds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.megamart.R;
import com.example.megamart.managefunds.collab.CollabActivity;
import com.example.megamart.managefunds.donation.DonateActivity;

public class ManageFundsActivity extends AppCompatActivity {


    ImageView collab,donate,expense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_funds);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        collab  = findViewById(R.id.collab);
        donate = findViewById(R.id.donate);
        expense = findViewById(R.id.expense);


        collab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageFundsActivity.this, CollabActivity.class);
                startActivity(intent);

            }
        });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageFundsActivity.this, DonateActivity.class);
                startActivity(intent);
            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageFundsActivity.this, ExpenseActivity.class);
                startActivity(intent);
            }
        });

    }
}