package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.Candidat;
import com.example.electoralstatsapp.sqlite.models.Election;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class StatsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        // 1. Spinner cycle électoral
        Spinner spinnerCycle = view.findViewById(R.id.spinner_cycle);
        List<Election> elections = dbHelper.getAllElections();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        for (Election e : elections) {
            adapter.add(e.getType());
        }
        spinnerCycle.setAdapter(adapter);

        // 2. Progression (%)
        int totalVotants = dbHelper.getTotalVotants();
        int totalInscrits = dbHelper.getTotalInscrits();
        int progress = (totalInscrits > 0) ? (int) ((totalVotants * 100.0f) / totalInscrits) : 0;
        TextView tvProgress = view.findViewById(R.id.tv_progress);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        tvProgress.setText(progress + "%");
        progressBar.setProgress(progress);

        // 3. Top 3 candidats
        List<Candidat> candidats = dbHelper.getAllCandidats();
        // Trie les candidats par pourcentage décroissant
        Collections.sort(candidats, (a, b) -> Float.compare(
            dbHelper.getPourcentageForCandidat(b.getId()),
            dbHelper.getPourcentageForCandidat(a.getId())
        ));
        // Affiche les 3 premiers
        if (candidats.size() > 0) {
            Candidat c1 = candidats.get(0);
            ((TextView) view.findViewById(R.id.tv_nom_candidat1)).setText(c1.getPrenom() + " " + c1.getNom());
            int resId1 = getResources().getIdentifier(c1.getPhoto(), "drawable", getContext().getPackageName());
            ((ImageView) view.findViewById(R.id.img_candidat1)).setImageResource(resId1 != 0 ? resId1 : R.drawable.profilhomme);
        }
        if (candidats.size() > 1) {
            Candidat c2 = candidats.get(1);
            ((TextView) view.findViewById(R.id.tv_nom_candidat2)).setText(c2.getPrenom() + " " + c2.getNom());
            int resId2 = getResources().getIdentifier(c2.getPhoto(), "drawable", getContext().getPackageName());
            ((ImageView) view.findViewById(R.id.img_candidat2)).setImageResource(resId2 != 0 ? resId2 : R.drawable.profilhomme);
        }
        if (candidats.size() > 2) {
            Candidat c3 = candidats.get(2);
            ((TextView) view.findViewById(R.id.tv_nom_candidat3)).setText(c3.getPrenom() + " " + c3.getNom());
            int resId3 = getResources().getIdentifier(c3.getPhoto(), "drawable", getContext().getPackageName());
            ((ImageView) view.findViewById(R.id.img_candidat3)).setImageResource(resId3 != 0 ? resId3 : R.drawable.profilhomme);
        }

        // 4. Graphique barres (votes par jour)
        BarChart barChart = new BarChart(getContext());
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        Map<String, Integer> votesParJour = dbHelper.getVotesParJour();
        int i = 0;
        for (Map.Entry<String, Integer> entry : votesParJour.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue()));
            days.add(entry.getKey());
            i++;
        }
        BarDataSet dataSet = new BarDataSet(entries, "Votes");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.invalidate();
        ViewGroup chartContainer = view.findViewById(R.id.chart_votes);
        chartContainer.removeAllViews();
        chartContainer.addView(barChart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return view;
    }
} 