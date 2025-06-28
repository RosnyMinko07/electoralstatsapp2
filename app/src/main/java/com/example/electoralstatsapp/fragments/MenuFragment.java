package com.example.electoralstatsapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.fragments.StatsFragment;
import com.example.electoralstatsapp.fragments.Fragment_type_election;
import com.example.electoralstatsapp.fragments.Fragment_resultats_des_elections;
import com.example.electoralstatsapp.fragments.CandidatsFragment;
import com.example.electoralstatsapp.fragments.GestionUtilisateursFragment;
import com.example.electoralstatsapp.fragments.Fragment_comparaison_resultats;
import com.example.electoralstatsapp.fragments.Fragment_gestion_elections;
import com.example.electoralstatsapp.fragments.Fragment_gestion_bureaux;
import com.example.electoralstatsapp.fragments.Fragment_gestion_centres;
import com.example.electoralstatsapp.fragments.Fragment_gestion_circonscriptions;
import com.example.electoralstatsapp.fragments.Fragment_presentation_des_resultats;
import com.example.electoralstatsapp.fragments.FragmentNotifications;
import com.example.electoralstatsapp.fragments.ResultEntryFragment;

public class MenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userRole = prefs.getString("user_role", "");

        Button btnResultat = view.findViewById(R.id.btn_resultat);
        Button btnGestion = view.findViewById(R.id.btn_gestion);
        Button btnGestionCandidats = view.findViewById(R.id.btn_gestion_candidats);
        Button btnSaisieResultats = view.findViewById(R.id.btn_saisie_resultats);
        Button btnStats = view.findViewById(R.id.btn_stats);
        LinearLayout btnLogout = view.findViewById(R.id.btn_logout);
        ImageView btnNotifications = view.findViewById(R.id.btn_notifications);
        Button btnGestionUtilisateursAdmin = view.findViewById(R.id.btn_gestion_utilisateurs_admin);
        Button btnGestionElections = view.findViewById(R.id.btn_gestion_elections);
        Button btnGestionBureaux = view.findViewById(R.id.btn_gestion_bureaux);
        Button btnGestionCentres = view.findViewById(R.id.btn_gestion_centres);
        Button btnGestionCirconscriptions = view.findViewById(R.id.btn_gestion_circonscriptions);
        Button btnPresentationResultats = view.findViewById(R.id.btn_presentation_resultats);
        final View progressBar = view.findViewById(R.id.progress_loading);

        // Masquer tous les boutons par défaut
        btnResultat.setVisibility(View.GONE);
        btnGestion.setVisibility(View.GONE);
        btnGestionCandidats.setVisibility(View.GONE);
        btnSaisieResultats.setVisibility(View.GONE);
        btnGestionUtilisateursAdmin.setVisibility(View.GONE);
        view.findViewById(R.id.btn_comparaison).setVisibility(View.GONE);
        btnStats.setVisibility(View.GONE);
        btnGestionElections.setVisibility(View.GONE);
        btnGestionBureaux.setVisibility(View.GONE);
        btnGestionCentres.setVisibility(View.GONE);
        btnGestionCirconscriptions.setVisibility(View.GONE);
        btnPresentationResultats.setVisibility(View.GONE);
        btnLogout.setVisibility(View.VISIBLE);

        if (userRole.equals("Administrateur")) {
            btnResultat.setVisibility(View.VISIBLE);
            btnGestion.setVisibility(View.VISIBLE);
            btnGestionCandidats.setVisibility(View.VISIBLE);
            btnSaisieResultats.setVisibility(View.VISIBLE);
            btnGestionUtilisateursAdmin.setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_comparaison).setVisibility(View.VISIBLE);
            btnStats.setVisibility(View.VISIBLE);
            btnGestionElections.setVisibility(View.VISIBLE);
            btnGestionBureaux.setVisibility(View.VISIBLE);
            btnGestionCentres.setVisibility(View.VISIBLE);
            btnGestionCirconscriptions.setVisibility(View.VISIBLE);
            btnPresentationResultats.setVisibility(View.VISIBLE);
        } else if (userRole.equals("superviseur")) {
            btnResultat.setVisibility(View.VISIBLE);
            btnGestionCandidats.setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_comparaison).setVisibility(View.VISIBLE);
            btnStats.setVisibility(View.VISIBLE);
            btnPresentationResultats.setVisibility(View.VISIBLE);
        } else if (userRole.equals("opérateur")) {
            btnSaisieResultats.setVisibility(View.VISIBLE);
            btnPresentationResultats.setVisibility(View.VISIBLE);
        } else if (userRole.equals("électeur")) {
            btnResultat.setVisibility(View.VISIBLE);
            btnPresentationResultats.setVisibility(View.VISIBLE);
        }

        btnNotifications.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new FragmentNotifications()));
        btnStats.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new StatsFragment()));
        btnLogout.setOnClickListener(v -> {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            FragmentManager fm = getParentFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragment_container, new LoginFragment());
            transaction.commit();
            view.postDelayed(() -> { if (progressBar != null) progressBar.setVisibility(View.GONE); }, 600);
        });
        btnSaisieResultats.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new ResultEntryFragment()));
        btnResultat.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new Fragment_resultats_des_elections()));
        btnGestion.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new Fragment_type_election()));
        btnGestionCandidats.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new CandidatsFragment()));
        btnGestionUtilisateursAdmin.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new GestionUtilisateursFragment()));
        view.findViewById(R.id.btn_comparaison).setOnClickListener(v -> showLoadingAndNavigate(progressBar, new Fragment_comparaison_resultats()));
        btnGestionElections.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new Fragment_gestion_elections()));
        btnGestionBureaux.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new Fragment_gestion_bureaux()));
        btnGestionCentres.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new Fragment_gestion_centres()));
        btnGestionCirconscriptions.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new Fragment_gestion_circonscriptions()));
        btnPresentationResultats.setOnClickListener(v -> showLoadingAndNavigate(progressBar, new Fragment_presentation_des_resultats()));
    }

    private void showLoadingAndNavigate(View progressBar, Fragment fragment) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        navigateTo(fragment);
        if (getView() != null) {
            getView().postDelayed(() -> {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }, 600);
        }
    }

    private void navigateTo(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
