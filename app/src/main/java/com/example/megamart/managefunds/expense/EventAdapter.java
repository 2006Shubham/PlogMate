package com.example.megamart.managefunds.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    Context context;
    List<EventModel> eventList;
    OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(EventModel model);
    }

    public EventAdapter(Context context, List<EventModel> eventList, OnEventClickListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventModel model = eventList.get(position);
        ((TextView) holder.itemView).setText(model.getEvent_name());

        holder.itemView.setOnClickListener(v -> listener.onEventClick(model));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
