package com.example.megamart.managefunds.expense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.megamart.R;

import java.util.List;

public class ExpenseDetailAdapter extends RecyclerView.Adapter<ExpenseDetailAdapter.ExpenseViewHolder> {

    List<TotalExpenseActivity.ExpenseDetail> list;

    public ExpenseDetailAdapter(List<TotalExpenseActivity.ExpenseDetail> list) {
        this.list = list;
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvExpenseName);
            amount = itemView.findViewById(R.id.tvExpenseAmount);
        }
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_expense_item_row_recycler, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        TotalExpenseActivity.ExpenseDetail e = list.get(position);
        holder.name.setText(e.name);
        holder.amount.setText("₹" + e.amount);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
