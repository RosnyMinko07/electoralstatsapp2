package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.Circonscription;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.electoralstatsapp.adapters.CirconscriptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_gestion_circonscriptions extends Fragment {
    private RecyclerView recyclerView;
    private CirconscriptionAdapter adapter;
    private List<Circonscription> circonscriptionList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gestion_circonscriptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_circonscriptions);
        fab = view.findViewById(R.id.fab_add_circonscription);
        setupRecyclerView();
        loadCirconscriptions();
        fab.setOnClickListener(v -> showAddCirconscriptionDialog());
    }

    private void setupRecyclerView() {
        circonscriptionList = new ArrayList<>();
        adapter = new CirconscriptionAdapter(circonscriptionList, new CirconscriptionAdapter.OnCirconscriptionActionListener() {
            @Override
            public void onEdit(Circonscription circ) {
                showEditCirconscriptionDialog(circ);
            }
            @Override
            public void onDelete(Circonscription circ) {
                showDeleteCirconscriptionDialog(circ);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadCirconscriptions() {
        if (adapter != null) {
            circonscriptionList.clear();
            circonscriptionList.addAll(dbHelper.getAllCirconscriptions());
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddCirconscriptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_circonscription, null);
        final EditText etNom = dialogView.findViewById(R.id.dialog_et_circonscription_nom);
        final EditText etRegion = dialogView.findViewById(R.id.dialog_et_circonscription_region);
        final EditText etNbInscrits = dialogView.findViewById(R.id.dialog_et_circonscription_nb_inscrits);
        builder.setView(dialogView)
                .setTitle("Nouvelle circonscription")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String nom = etNom.getText().toString().trim();
                    String region = etRegion.getText().toString().trim();
                    int nbInscrits = parseIntSafe(etNbInscrits.getText().toString().trim());
                    if (nom.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Circonscription circ = new Circonscription();
                    circ.setNom(nom);
                    circ.setRegion(region);
                    circ.setNbInscrits(nbInscrits);
                    dbHelper.addCirconscription(circ);
                    loadCirconscriptions();
                    Toast.makeText(getContext(), "Circonscription ajoutée !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private void showEditCirconscriptionDialog(Circonscription circ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_circonscription, null);
        final EditText etNom = dialogView.findViewById(R.id.dialog_et_circonscription_nom);
        final EditText etRegion = dialogView.findViewById(R.id.dialog_et_circonscription_region);
        final EditText etNbInscrits = dialogView.findViewById(R.id.dialog_et_circonscription_nb_inscrits);
        etNom.setText(circ.getNom());
        etRegion.setText(circ.getRegion());
        etNbInscrits.setText(String.valueOf(circ.getNbInscrits()));
        builder.setView(dialogView)
                .setTitle("Modifier la circonscription")
                .setPositiveButton("Enregistrer", (dialog, id) -> {
                    String nom = etNom.getText().toString().trim();
                    String region = etRegion.getText().toString().trim();
                    int nbInscrits = parseIntSafe(etNbInscrits.getText().toString().trim());
                    if (nom.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    circ.setNom(nom);
                    circ.setRegion(region);
                    circ.setNbInscrits(nbInscrits);
                    dbHelper.updateCirconscription(circ);
                    loadCirconscriptions();
                    Toast.makeText(getContext(), "Circonscription modifiée !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private void showDeleteCirconscriptionDialog(Circonscription circ) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Supprimer la circonscription")
                .setMessage("Voulez-vous vraiment supprimer cette circonscription ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    dbHelper.deleteCirconscription(circ.getId());
                    loadCirconscriptions();
                    Toast.makeText(getContext(), "Circonscription supprimée !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
} 