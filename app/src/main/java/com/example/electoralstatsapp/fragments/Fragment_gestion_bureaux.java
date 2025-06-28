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
import com.example.electoralstatsapp.sqlite.models.BureauDeVote;
import com.example.electoralstatsapp.adapters.BureauAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Fragment_gestion_bureaux extends Fragment {
    private RecyclerView recyclerView;
    private BureauAdapter adapter;
    private List<BureauDeVote> bureauList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gestion_bureaux, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_bureaux);
        fab = view.findViewById(R.id.fab_add_bureau);
        setupRecyclerView();
        loadBureaux();
        fab.setOnClickListener(v -> showAddBureauDialog());
    }

    private void setupRecyclerView() {
        bureauList = new ArrayList<>();
        adapter = new BureauAdapter(bureauList, new BureauAdapter.OnBureauActionListener() {
            @Override
            public void onEdit(BureauDeVote bureau) {
                showEditBureauDialog(bureau);
            }
            @Override
            public void onDelete(BureauDeVote bureau) {
                showDeleteBureauDialog(bureau);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadBureaux() {
        if (adapter != null) {
            bureauList.clear();
            bureauList.addAll(dbHelper.getAllBureaux());
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddBureauDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_bureau, null);
        final EditText etNumero = dialogView.findViewById(R.id.dialog_et_bureau_numero);
        final EditText etNbInscrits = dialogView.findViewById(R.id.dialog_et_bureau_nb_inscrits);
        final EditText etNbVotants = dialogView.findViewById(R.id.dialog_et_bureau_nb_votants);
        final EditText etCentre = dialogView.findViewById(R.id.dialog_et_bureau_centre);
        builder.setView(dialogView)
                .setTitle("Nouveau bureau de vote")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String numero = etNumero.getText().toString().trim();
                    int nbInscrits = parseIntSafe(etNbInscrits.getText().toString().trim());
                    int nbVotants = parseIntSafe(etNbVotants.getText().toString().trim());
                    long centreId = parseLongSafe(etCentre.getText().toString().trim());
                    if (numero.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BureauDeVote bureau = new BureauDeVote();
                    bureau.setNumero(numero);
                    bureau.setNbInscrits(nbInscrits);
                    bureau.setNbVotants(nbVotants);
                    bureau.setCentreDeVoteId(centreId);
                    long result = dbHelper.addBureau(bureau);
                    if (result == -1) {
                        Toast.makeText(getContext(), "Erreur lors de l'ajout du bureau (centre inexistant ?)", Toast.LENGTH_LONG).show();
                    } else {
                        loadBureaux();
                        Toast.makeText(getContext(), "Bureau ajouté !", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private void showEditBureauDialog(BureauDeVote bureau) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_bureau, null);
        final EditText etNumero = dialogView.findViewById(R.id.dialog_et_bureau_numero);
        final EditText etNbInscrits = dialogView.findViewById(R.id.dialog_et_bureau_nb_inscrits);
        final EditText etNbVotants = dialogView.findViewById(R.id.dialog_et_bureau_nb_votants);
        final EditText etCentre = dialogView.findViewById(R.id.dialog_et_bureau_centre);
        etNumero.setText(bureau.getNumero());
        etNbInscrits.setText(String.valueOf(bureau.getNbInscrits()));
        etNbVotants.setText(String.valueOf(bureau.getNbVotants()));
        etCentre.setText(String.valueOf(bureau.getCentreDeVoteId()));
        builder.setView(dialogView)
                .setTitle("Modifier le bureau")
                .setPositiveButton("Enregistrer", (dialog, id) -> {
                    String numero = etNumero.getText().toString().trim();
                    int nbInscrits = parseIntSafe(etNbInscrits.getText().toString().trim());
                    int nbVotants = parseIntSafe(etNbVotants.getText().toString().trim());
                    long centreId = parseLongSafe(etCentre.getText().toString().trim());
                    if (numero.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bureau.setNumero(numero);
                    bureau.setNbInscrits(nbInscrits);
                    bureau.setNbVotants(nbVotants);
                    bureau.setCentreDeVoteId(centreId);
                    dbHelper.updateBureau(bureau);
                    loadBureaux();
                    Toast.makeText(getContext(), "Bureau modifié !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private long parseLongSafe(String s) {
        try { return Long.parseLong(s); } catch (Exception e) { return 0; }
    }

    private void showDeleteBureauDialog(BureauDeVote bureau) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Supprimer le bureau")
                .setMessage("Voulez-vous vraiment supprimer ce bureau ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    dbHelper.deleteBureau(bureau.getId());
                    loadBureaux();
                    Toast.makeText(getContext(), "Bureau supprimé !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
} 