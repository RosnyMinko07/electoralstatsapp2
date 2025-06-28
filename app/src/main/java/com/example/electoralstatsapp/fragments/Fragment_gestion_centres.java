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
import com.example.electoralstatsapp.sqlite.models.CentreDeVote;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.electoralstatsapp.adapters.CentreAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_gestion_centres extends Fragment {
    private RecyclerView recyclerView;
    private CentreAdapter adapter;
    private List<CentreDeVote> centreList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gestion_centres, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_centres);
        fab = view.findViewById(R.id.fab_add_centre);
        setupRecyclerView();
        loadCentres();
        fab.setOnClickListener(v -> showAddCentreDialog());
    }

    private void setupRecyclerView() {
        centreList = new ArrayList<>();
        adapter = new CentreAdapter(centreList, new CentreAdapter.OnCentreActionListener() {
            @Override
            public void onEdit(CentreDeVote centre) {
                showEditCentreDialog(centre);
            }
            @Override
            public void onDelete(CentreDeVote centre) {
                showDeleteCentreDialog(centre);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadCentres() {
        if (adapter != null) {
            centreList.clear();
            centreList.addAll(dbHelper.getAllCentres());
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddCentreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_centre, null);
        final EditText etNom = dialogView.findViewById(R.id.dialog_et_centre_nom);
        final EditText etAdresse = dialogView.findViewById(R.id.dialog_et_centre_adresse);
        final EditText etLat = dialogView.findViewById(R.id.dialog_et_centre_latitude);
        final EditText etLong = dialogView.findViewById(R.id.dialog_et_centre_longitude);
        final EditText etCirc = dialogView.findViewById(R.id.dialog_et_centre_circonscription);
        builder.setView(dialogView)
                .setTitle("Nouveau centre de vote")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String nom = etNom.getText().toString().trim();
                    String adresse = etAdresse.getText().toString().trim();
                    double latitude = parseDoubleSafe(etLat.getText().toString().trim());
                    double longitude = parseDoubleSafe(etLong.getText().toString().trim());
                    long circId = parseLongSafe(etCirc.getText().toString().trim());
                    if (nom.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    CentreDeVote centre = new CentreDeVote();
                    centre.setNom(nom);
                    centre.setAdresse(adresse);
                    centre.setLatitude(latitude);
                    centre.setLongitude(longitude);
                    centre.setCirconscriptionId(circId);
                    long result = dbHelper.addCentre(centre);
                    if (result == -1) {
                        Toast.makeText(getContext(), "Erreur lors de l'ajout du centre (circonscription inexistante ?)", Toast.LENGTH_LONG).show();
                    } else {
                        loadCentres();
                        Toast.makeText(getContext(), "Centre ajouté !", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private void showEditCentreDialog(CentreDeVote centre) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_centre, null);
        final EditText etNom = dialogView.findViewById(R.id.dialog_et_centre_nom);
        final EditText etAdresse = dialogView.findViewById(R.id.dialog_et_centre_adresse);
        final EditText etLat = dialogView.findViewById(R.id.dialog_et_centre_latitude);
        final EditText etLong = dialogView.findViewById(R.id.dialog_et_centre_longitude);
        final EditText etCirc = dialogView.findViewById(R.id.dialog_et_centre_circonscription);
        etNom.setText(centre.getNom());
        etAdresse.setText(centre.getAdresse());
        etLat.setText(String.valueOf(centre.getLatitude()));
        etLong.setText(String.valueOf(centre.getLongitude()));
        etCirc.setText(String.valueOf(centre.getCirconscriptionId()));
        builder.setView(dialogView)
                .setTitle("Modifier le centre")
                .setPositiveButton("Enregistrer", (dialog, id) -> {
                    String nom = etNom.getText().toString().trim();
                    String adresse = etAdresse.getText().toString().trim();
                    double latitude = parseDoubleSafe(etLat.getText().toString().trim());
                    double longitude = parseDoubleSafe(etLong.getText().toString().trim());
                    long circId = parseLongSafe(etCirc.getText().toString().trim());
                    if (nom.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    centre.setNom(nom);
                    centre.setAdresse(adresse);
                    centre.setLatitude(latitude);
                    centre.setLongitude(longitude);
                    centre.setCirconscriptionId(circId);
                    dbHelper.updateCentre(centre);
                    loadCentres();
                    Toast.makeText(getContext(), "Centre modifié !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0; }
    }

    private long parseLongSafe(String s) {
        try { return Long.parseLong(s); } catch (Exception e) { return 0; }
    }

    private void showDeleteCentreDialog(CentreDeVote centre) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Supprimer le centre")
                .setMessage("Voulez-vous vraiment supprimer ce centre ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    dbHelper.deleteCentre(centre.getId());
                    loadCentres();
                    Toast.makeText(getContext(), "Centre supprimé !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
} 