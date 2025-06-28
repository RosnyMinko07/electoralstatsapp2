package com.example.electoralstatsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.models.Election;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ElectionAdapter extends RecyclerView.Adapter<ElectionAdapter.ElectionViewHolder> {

    public interface OnElectionActionListener {
        void onEdit(Election election);
        void onDelete(Election election);
    }

    private List<Election> electionList;
    private OnElectionActionListener listener;

    public ElectionAdapter(List<Election> electionList, OnElectionActionListener listener) {
        this.electionList = electionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ElectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_election, parent, false);
        return new ElectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectionViewHolder holder, int position) {
        Election election = electionList.get(position);
        holder.typeTextView.setText(election.getType());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        holder.dateTextView.setText(sdf.format(election.getDateScrutin()));

        holder.editImageView.setOnClickListener(v -> listener.onEdit(election));
        holder.deleteImageView.setOnClickListener(v -> listener.onDelete(election));
    }

    @Override
    public int getItemCount() {
        return electionList.size();
    }

    static class ElectionViewHolder extends RecyclerView.ViewHolder {
        TextView typeTextView;
        TextView dateTextView;
        ImageView editImageView;
        ImageView deleteImageView;

        public ElectionViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.item_election_type);
            dateTextView = itemView.findViewById(R.id.item_election_date);
            editImageView = itemView.findViewById(R.id.item_election_edit);
            deleteImageView = itemView.findViewById(R.id.item_election_delete);
        }
    }
} 