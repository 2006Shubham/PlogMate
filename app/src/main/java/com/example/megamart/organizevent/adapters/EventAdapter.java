package com.example.megamart.organizevent.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megamart.R;
import com.example.megamart.editevent.EditEventActivity;
import com.example.megamart.organizevent.datamodels.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;
    private ActivityResultLauncher<Intent> launcher;

    public EventAdapter(List<Event> eventList, Context context, ActivityResultLauncher<Intent> launcher) {
        this.eventList = eventList;
        this.context = context;
        this.launcher = launcher;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_history_recycler, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventName.setText(event.getName());
        holder.eventDate.setText(event.getDate());
        holder.eventLocation.setText(event.getLocation());
        holder.eventDescription.setText(event.getDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditEventActivity.class);
            intent.putExtra("event", event);
            intent.putExtra("position", position);
            launcher.launch(intent); // Use launcher to start for result
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate, eventLocation, eventDescription;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventDescription = itemView.findViewById(R.id.eventDescription);
        }
    }
}
