package com.example.megamart.managefunds.collab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.megamart.R;
import java.util.ArrayList;

public class CollaboratorAdapter extends RecyclerView.Adapter<CollaboratorAdapter.CollaboratorViewHolder> {

    private ArrayList<Collaborator> collaborators;

    public CollaboratorAdapter(ArrayList<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    @Override
    public CollaboratorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_collaborator_recycler, parent, false);
        return new CollaboratorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollaboratorViewHolder holder, int position) {
        Collaborator collaborator = collaborators.get(position);
        holder.tvCollaboratorName.setText("Name : "+collaborator.getCollaboratorName());
        holder.tvOwner.setText("Owner : "+collaborator.getOwner());
        holder.tvPurpose.setText("Purpose : "+collaborator.getPurpose());
    }

    @Override
    public int getItemCount() {
        return collaborators.size();
    }

    public static class CollaboratorViewHolder extends RecyclerView.ViewHolder {
        TextView tvCollaboratorName, tvOwner, tvPurpose;

        public CollaboratorViewHolder(View itemView) {
            super(itemView);
            tvCollaboratorName = itemView.findViewById(R.id.tvCollaboratorName);
            tvOwner = itemView.findViewById(R.id.tvOwner);
            tvPurpose = itemView.findViewById(R.id.tvPurpose);
        }
    }
}
