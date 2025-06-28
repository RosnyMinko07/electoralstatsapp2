package com.example.electoralstatsapp.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.models.Candidat;
import java.util.List;

public class CandidatAdapter extends RecyclerView.Adapter<CandidatAdapter.CandidatViewHolder> {

    private List<Candidat> candidatList;

    public CandidatAdapter(List<Candidat> candidatList) {
        this.candidatList = candidatList;
    }

    @NonNull
    @Override
    public CandidatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidat, parent, false);
        return new CandidatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidatViewHolder holder, int position) {
        Candidat candidat = candidatList.get(position);
        String fullName = candidat.getPrenom() + " " + candidat.getNom();
        holder.nameTextView.setText(fullName);
        holder.partyTextView.setText(candidat.getParti());

        String photo = candidat.getPhoto();
        if (photo != null && photo.startsWith("content://")) {
            holder.photoImageView.setImageURI(Uri.parse(photo));
        } else {
            holder.photoImageView.setImageResource(R.drawable.profilhomme);
        }
    }

    @Override
    public int getItemCount() {
        return candidatList.size();
    }

    static class CandidatViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        TextView nameTextView;
        TextView partyTextView;

        public CandidatViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.item_candidat_photo);
            nameTextView = itemView.findViewById(R.id.item_candidat_name);
            partyTextView = itemView.findViewById(R.id.item_candidat_party);
        }
    }
} 