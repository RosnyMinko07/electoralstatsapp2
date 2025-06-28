package com.example.electoralstatsapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.models.ResultatView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ResultatAdapter extends RecyclerView.Adapter<ResultatAdapter.ResultatViewHolder> {

    private List<ResultatView> resultatList;

    public ResultatAdapter(List<ResultatView> resultatList) {
        this.resultatList = resultatList;
    }

    @NonNull
    @Override
    public ResultatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultat, parent, false);
        return new ResultatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultatViewHolder holder, int position) {
        ResultatView resultat = resultatList.get(position);

        holder.candidatTextView.setText(resultat.getCandidatFullName());

        String details = resultat.getElectionType() + " - Bureau #" + resultat.getBureauNumero();
        holder.detailsTextView.setText(details);

        // Formatter le nombre pour la lisibilité
        String formattedVoix = NumberFormat.getNumberInstance(Locale.FRANCE).format(resultat.getNbVoix());
        holder.voixTextView.setText(formattedVoix);
    }

    @Override
    public int getItemCount() {
        return resultatList.size();
    }

    static class ResultatViewHolder extends RecyclerView.ViewHolder {
        TextView candidatTextView;
        TextView detailsTextView;
        TextView voixTextView;

        public ResultatViewHolder(@NonNull View itemView) {
            super(itemView);
            candidatTextView = itemView.findViewById(R.id.item_resultat_candidat);
            detailsTextView = itemView.findViewById(R.id.item_resultat_details);
            voixTextView = itemView.findViewById(R.id.item_resultat_voix);
        }
    }
} 