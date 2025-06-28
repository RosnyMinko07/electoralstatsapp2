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
import com.example.electoralstatsapp.sqlite.models.Election;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.electoralstatsapp.adapters.ElectionAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_gestion_elections extends Fragment {
    private RecyclerView recyclerView;
    private ElectionAdapter adapter;
    private List<Election> electionList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gestion_elections, container, false);
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
                showEditElectionDialog(election);
            }
            @Override
            public void onDelete(Election election) {
                showDeleteElectionDialog(election);
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
        final EditText etDate = dialogView.findViewById(R.id.dialog_et_election_date);
        builder.setView(dialogView)
                .setTitle("Nouvelle élection")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String type = etType.getText().toString().trim();
                    String dateStr = etDate.getText().toString().trim();
                    if (type.isEmpty() || dateStr.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Election election = new Election();
                    election.setType(type);
                    election.setDateScrutin(parseDate(dateStr));
                    dbHelper.addElection(election);
                    loadElections();
                    Toast.makeText(getContext(), "Élection ajoutée !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private void showEditElectionDialog(Election election) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_election, null);
        final EditText etType = dialogView.findViewById(R.id.dialog_et_election_type);
        final EditText etDate = dialogView.findViewById(R.id.dialog_et_election_date);
        etType.setText(election.getType());
        etDate.setText(formatDate(election.getDateScrutin()));
        builder.setView(dialogView)
                .setTitle("Modifier l'élection")
                .setPositiveButton("Enregistrer", (dialog, id) -> {
                    String type = etType.getText().toString().trim();
                    String dateStr = etDate.getText().toString().trim();
                    if (type.isEmpty() || dateStr.isEmpty()) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    election.setType(type);
                    election.setDateScrutin(parseDate(dateStr));
                    dbHelper.updateElection(election);
                    loadElections();
                    Toast.makeText(getContext(), "Élection modifiée !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr);
        } catch (ParseException e) {
            return new Date();
        }
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    private void showDeleteElectionDialog(Election election) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Supprimer l'élection")
                .setMessage("Voulez-vous vraiment supprimer cette élection ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    dbHelper.deleteElection(election.getId());
                    loadElections();
                    Toast.makeText(getContext(), "Élection supprimée !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
} 