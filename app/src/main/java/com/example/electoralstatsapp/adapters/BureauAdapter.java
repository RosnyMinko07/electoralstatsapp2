package com.example.electoralstatsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.models.BureauDeVote;
import java.util.List;

public class BureauAdapter extends RecyclerView.Adapter<BureauAdapter.BureauViewHolder> {
    public interface OnBureauActionListener {
        void onEdit(BureauDeVote bureau);
        void onDelete(BureauDeVote bureau);
    }
    private List<BureauDeVote> bureauList;
    private OnBureauActionListener listener;
    public BureauAdapter(List<BureauDeVote> bureauList, OnBureauActionListener listener) {
        this.bureauList = bureauList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public BureauViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bureau, parent, false);
        return new BureauViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BureauViewHolder holder, int position) {
        BureauDeVote bureau = bureauList.get(position);
        holder.numeroTextView.setText(bureau.getNumero());
        holder.editImageView.setOnClickListener(v -> listener.onEdit(bureau));
        holder.deleteImageView.setOnClickListener(v -> listener.onDelete(bureau));
    }
    @Override
    public int getItemCount() {
        return bureauList.size();
    }
    static class BureauViewHolder extends RecyclerView.ViewHolder {
        TextView numeroTextView;
        ImageView editImageView, deleteImageView;
        public BureauViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroTextView = itemView.findViewById(R.id.item_bureau_numero);
            editImageView = itemView.findViewById(R.id.item_bureau_edit);
            deleteImageView = itemView.findViewById(R.id.item_bureau_delete);
        }
    }
} 