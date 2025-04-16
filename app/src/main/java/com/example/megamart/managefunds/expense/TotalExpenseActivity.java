package com.example.megamart.managefunds.expense;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.megamart.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TotalExpenseActivity extends AppCompatActivity {

    RecyclerView rvExpenseDetails;
    TextView tvEventName, tvTotalAmount;
    ExpenseDetailAdapter adapter;
    List<ExpenseDetail> expenseList = new ArrayList<>();

    String eventId, eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_expense);

        rvExpenseDetails = findViewById(R.id.rvExpenseDetails);
        tvEventName = findViewById(R.id.tvEventName);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        eventId = getIntent().getStringExtra("event_id");
        eventName = getIntent().getStringExtra("event_name");

        tvEventName.setText("Expenses for: " + eventName);

        adapter = new ExpenseDetailAdapter(expenseList);
        rvExpenseDetails.setLayoutManager(new LinearLayoutManager(this));
        rvExpenseDetails.setAdapter(adapter);

        fetchExpensesForEvent();
    }

    private void fetchExpensesForEvent() {
        String url = "http://192.168.150.81:7070/event_api/get_event_expenses.php?event_id=" + eventId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        expenseList.clear();
                        int total = 0;

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String name = obj.getString("expense_name");
                            int amount = obj.getInt("amount");

                            expenseList.add(new ExpenseDetail(name, amount));
                            total += amount;
                        }

                        adapter.notifyDataSetChanged();
                        tvTotalAmount.setText("Total: ₹" + total);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing expenses", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error fetching expenses", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        Volley.newRequestQueue(this).add(request);
    }

    // ExpenseDetail model class
    public static class ExpenseDetail {
        String name;
        int amount;

        public ExpenseDetail(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }
    }
}
