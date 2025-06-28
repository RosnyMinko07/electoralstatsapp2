package com.example.electoralstatsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.models.CentreDeVote;
import java.util.List;

public class CentreAdapter extends RecyclerView.Adapter<CentreAdapter.CentreViewHolder> {
    public interface OnCentreActionListener {
        void onEdit(CentreDeVote centre);
        void onDelete(CentreDeVote centre);
    }
    private List<CentreDeVote> centreList;
    private OnCentreActionListener listener;
    public CentreAdapter(List<CentreDeVote> centreList, OnCentreActionListener listener) {
        this.centreList = centreList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public CentreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_centre, parent, false);
        return new CentreViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CentreViewHolder holder, int position) {
        CentreDeVote centre = centreList.get(position);
        holder.nomTextView.setText(centre.getNom());
        holder.editImageView.setOnClickListener(v -> listener.onEdit(centre));
        holder.deleteImageView.setOnClickListener(v -> listener.onDelete(centre));
    }
    @Override
    public int getItemCount() {
        return centreList.size();
    }
    static class CentreViewHolder extends RecyclerView.ViewHolder {
        TextView nomTextView;
        ImageView editImageView, deleteImageView;
        public CentreViewHolder(@NonNull View itemView) {
            super(itemView);
            nomTextView = itemView.findViewById(R.id.item_centre_nom);
            editImageView = itemView.findViewById(R.id.item_centre_edit);
            deleteImageView = itemView.findViewById(R.id.item_centre_delete);
        }
    }
} 