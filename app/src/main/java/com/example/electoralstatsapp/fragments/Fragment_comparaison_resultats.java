package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.SharedPreferences;

public class Fragment_comparaison_resultats extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comparaison_resultats, container, false);
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userRole = prefs.getString("user_role", "");
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        // Filtres (exemple d'action)
        Button btnRegion = view.findViewById(R.id.btn_region);
        Button btnParti = view.findViewById(R.id.btn_parti);
        Button btnComparaison = view.findViewById(R.id.btn_comparaison);

        btnRegion.setOnClickListener(v -> {
            // TODO : Afficher les résultats par région
        });
        btnParti.setOnClickListener(v -> {
            // TODO : Afficher les résultats par parti
        });
        btnComparaison.setOnClickListener(v -> {
            // TODO : Afficher la comparaison globale
        });

        // Graphique barres (votes par région)
        BarChart barChart = new BarChart(getContext());
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> regions = new ArrayList<>();
        Map<String, Integer> votesParRegion = dbHelper.getVotesParRegion();
        int i = 0;
        for (Map.Entry<String, Integer> entry : votesParRegion.entrySet()) {
            barEntries.add(new BarEntry(i, entry.getValue()));
            regions.add(entry.getKey());
            i++;
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Votes par région");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(regions));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.invalidate();
        ViewGroup chartBarres = view.findViewById(R.id.chart_barres);
        chartBarres.removeAllViews();
        chartBarres.addView(barChart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Camembert (votes par parti)
        PieChart pieChart = new PieChart(getContext());
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Integer> votesParParti = dbHelper.getVotesParParti();
        for (Map.Entry<String, Integer> entry : votesParParti.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Répartition des votes");
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
        ViewGroup chartPie = view.findViewById(R.id.chart_pie);
        chartPie.removeAllViews();
        chartPie.addView(pieChart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Switchs
        Switch switchValider = view.findViewById(R.id.switch_valider);
        Switch switchDivergence = view.findViewById(R.id.switch_divergence);

        if (!"superviseur".equalsIgnoreCase(userRole)) {
            switchValider.setVisibility(View.GONE);
        }
        switchValider.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO : Action de validation
        });
        switchDivergence.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO : Action de signalement
        });

        return view;
    }
} 