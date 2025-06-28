package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.electoralstatsapp.adapters.ElectionAdapter;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.Election;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_type_election extends Fragment {

    private RecyclerView recyclerView;
    private ElectionAdapter adapter;
    private List<Election> electionList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_type_election, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_elections);
        fab = view.findViewById(R.id.fab_add_election);

        setupRecyclerView();
        loadElections();

        fab.setOnClickListener(v -> showAddElectionDialog());
    }

    private void setupRecyclerView() {
        electionList = new ArrayList<>();
        adapter = new ElectionAdapter(electionList, new ElectionAdapter.OnElectionActionListener() {
            @Override
            public void onEdit(Election election) {
                // TODO: Ajouter la logique d'édition si besoin
            }
            @Override
            public void onDelete(Election election) {
                // TODO: Ajouter la logique de suppression si besoin
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadElections() {
        if (adapter != null) {
            electionList.clear();
            electionList.addAll(dbHelper.getAllElections());
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddElectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_election, null);

        final EditText etType = dialogView.findViewById(R.id.dialog_et_election_type);
        final EditText etStatut = dialogView.findViewById(R.id.dialog_et_election_statut);
        final EditText etTours = dialogView.findViewById(R.id.dialog_et_election_tours);

        builder.setView(dialogView)
                .setTitle("Nouvelle Élection")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String type = etType.getText().toString().trim();
                    String statut = etStatut.getText().toString().trim();
                    String toursStr = etTours.getText().toString().trim();

                    if (TextUtils.isEmpty(type) || TextUtils.isEmpty(statut) || TextUtils.isEmpty(toursStr)) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Election newElection = new Election();
                    newElection.setType(type);
                    newElection.setStatut(statut);
                    newElection.setNbTours(Integer.parseInt(toursStr));
                    newElection.setDateScrutin(new Date()); // Date actuelle pour la simplicité

                    dbHelper.addElection(newElection);
                    loadElections(); // Rafraîchir la liste
                    Toast.makeText(getContext(), "Élection ajoutée !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
