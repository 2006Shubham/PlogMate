package com.example.megamart.managefunds.donation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megamart.R;

import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {
    private List<Donor> donorList;

    public DonorAdapter(List<Donor> donorList) {
        this.donorList = donorList;
    }

    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_donor_recycler, parent, false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        Donor donor = donorList.get(position);
        holder.name.setText("Name: " + donor.getDonor_name());
        holder.amount.setText("Amount: ₹" + donor.getAmount());
        holder.mobile.setText("Mobile: " + donor.getMobile());
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public static class DonorViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount, mobile;

        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvDonorName);
            amount = itemView.findViewById(R.id.tvDonorAmount);
            mobile = itemView.findViewById(R.id.tvDonorMobile);
        }
    }
}
