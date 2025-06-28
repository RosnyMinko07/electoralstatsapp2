package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.electoralstatsapp.MainActivity;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;

public class Fragment_resultats_des_elections extends Fragment {

    public Fragment_resultats_des_elections() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultats_des_elections, container, false);
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "Utilisateur");
        TextView tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserName.setText(userName);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Affiche la barre de navigation
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNav();
        }

        ImageView btnMenu = view.findViewById(R.id.btn_menu);
        Button btnApercu = view.findViewById(R.id.btn_apercu);
        Button btnCandidat = view.findViewById(R.id.btn_candidat);

        // --- Ajout dynamique du taux de participation ---
        TextView tvTaux = view.findViewById(R.id.tv_taux_participation);
        ProgressBar progressTaux = view.findViewById(R.id.progress_taux_participation);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        int totalInscrits = dbHelper.getTotalInscrits();
        int totalVotants = dbHelper.getTotalVotants();
        int taux = 0;
        if (totalInscrits > 0) {
            taux = (int) Math.round((totalVotants * 100.0) / totalInscrits);
        }
        tvTaux.setText(taux + "%");
        progressTaux.setProgress(taux);
        // ---

        btnCandidat.setOnClickListener(v -> navigateTo(new CandidatsFragment()));
        btnApercu.setOnClickListener(v -> navigateTo(new Fragment_presentation_des_resultats()));
        btnMenu.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Menu cliqué", Toast.LENGTH_SHORT).show();
            navigateTo(new MenuFragment());
        });
    }

    private void navigateTo(Fragment fragment) {
        if (isAdded() && getActivity() != null) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
} 