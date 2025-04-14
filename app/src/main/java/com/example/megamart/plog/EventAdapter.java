package com.example.megamart.plog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megamart.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<Event> eventList;
    Fragment fragment;

    public EventAdapter(Context context, List<Event> eventList, Fragment fragment) {
        this.context = context;
        this.eventList = eventList;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_plog_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.tvEventName.setText(event.getEvent_name());
        // You can set other event details if needed

        // Decode Base64 image string and set it to the ImageView
        String encodedImage = event.getEvent_image();  // Get the Base64 image string
        if (encodedImage != null && !encodedImage.isEmpty()) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.eventImage.setImageBitmap(decodedBitmap);  // Set decoded image to ImageView
        } else {
            holder.eventImage.setImageResource(R.drawable.drive);  // Fallback image if no image is set
        }

        // Set onClickListener for eventCard to open EventDetailActivity
        holder.eventCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.putExtra("event_data", event); // Pass object to detail activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvEventDate, tvEventTime, tvAddress, tvCoordinator, tvMobile, tvDescription;
        CardView eventCard;
        ImageView eventImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            // Initialize other TextViews if needed
            eventImage = itemView.findViewById(R.id.eventImage);  // ImageView for event image
            eventCard = itemView.findViewById(R.id.eventCard);  // CardView for event item
        }
    }
}
