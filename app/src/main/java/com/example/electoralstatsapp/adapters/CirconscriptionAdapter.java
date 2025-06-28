package com.example.electoralstatsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.models.Circonscription;
import java.util.List;

public class CirconscriptionAdapter extends RecyclerView.Adapter<CirconscriptionAdapter.CirconscriptionViewHolder> {
    public interface OnCirconscriptionActionListener {
        void onEdit(Circonscription circ);
        void onDelete(Circonscription circ);
    }
    private List<Circonscription> circonscriptionList;
    private OnCirconscriptionActionListener listener;
    public CirconscriptionAdapter(List<Circonscription> circonscriptionList, OnCirconscriptionActionListener listener) {
        this.circonscriptionList = circonscriptionList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public CirconscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circonscription, parent, false);
        return new CirconscriptionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CirconscriptionViewHolder holder, int position) {
        Circonscription circ = circonscriptionList.get(position);
        holder.nomTextView.setText(circ.getNom());
        holder.editImageView.setOnClickListener(v -> listener.onEdit(circ));
        holder.deleteImageView.setOnClickListener(v -> listener.onDelete(circ));
    }
    @Override
    public int getItemCount() {
        return circonscriptionList.size();
    }
    static class CirconscriptionViewHolder extends RecyclerView.ViewHolder {
        TextView nomTextView;
        ImageView editImageView, deleteImageView;
        public CirconscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            nomTextView = itemView.findViewById(R.id.item_circonscription_nom);
            editImageView = itemView.findViewById(R.id.item_circonscription_edit);
            deleteImageView = itemView.findViewById(R.id.item_circonscription_delete);
        }
    }
} 