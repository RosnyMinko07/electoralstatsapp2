package com.example.electoralstatsapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.electoralstatsapp.adapters.UserAdapter;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GestionUtilisateursFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;
    private String userRole;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_candidats);
        fab = view.findViewById(R.id.fab_add_candidat);

        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userRole = prefs.getString("user_role", "");

        setupRecyclerView();
        loadUsers();

        fab.setOnClickListener(v -> showAddUserDialog());
    }

    private void setupRecyclerView() {
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList, userRole, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEdit(User user) {
                showEditUserDialog(user);
            }
            @Override
            public void onDelete(User user) {
                showDeleteUserDialog(user);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadUsers() {
        if (adapter != null) {
            userList.clear();
            userList.addAll(dbHelper.getAllUsers());
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_candidat, null);

        final EditText etPrenom = dialogView.findViewById(R.id.dialog_et_candidat_prenom);
        final EditText etNom = dialogView.findViewById(R.id.dialog_et_candidat_nom);
        final EditText etParti = dialogView.findViewById(R.id.dialog_et_candidat_parti);
        final EditText etBio = dialogView.findViewById(R.id.dialog_et_candidat_bio);

        etParti.setHint("Rôle (ex: citoyen, superviseur, opérateur, Administrateur)");
        etBio.setVisibility(View.GONE);

        builder.setView(dialogView)
                .setTitle("Nouvel utilisateur")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String prenom = etPrenom.getText().toString().trim();
                    String nom = etNom.getText().toString().trim();
                    String role = etParti.getText().toString().trim();
                    if (TextUtils.isEmpty(prenom) || TextUtils.isEmpty(nom) || TextUtils.isEmpty(role)) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String email = prenom.toLowerCase() + "." + nom.toLowerCase() + "@" + role.toLowerCase() + ".com";
                    String password = "password";
                    User newUser = new User(prenom + " " + nom, email, password, role);
                    dbHelper.addUser(newUser);
                    loadUsers();
                    Toast.makeText(getContext(), "Utilisateur ajouté !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showEditUserDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_candidat, null);

        final EditText etPrenom = dialogView.findViewById(R.id.dialog_et_candidat_prenom);
        final EditText etNom = dialogView.findViewById(R.id.dialog_et_candidat_nom);
        final EditText etParti = dialogView.findViewById(R.id.dialog_et_candidat_parti);
        final EditText etBio = dialogView.findViewById(R.id.dialog_et_candidat_bio);

        etBio.setVisibility(View.GONE);

        String[] names = user.getFullName().split(" ", 2);
        etPrenom.setText(names.length > 0 ? names[0] : "");
        etNom.setText(names.length > 1 ? names[1] : "");
        etParti.setText(user.getRole());

        builder.setView(dialogView)
                .setTitle("Modifier utilisateur")
                .setPositiveButton("Enregistrer", (dialog, id) -> {
                    String prenom = etPrenom.getText().toString().trim();
                    String nom = etNom.getText().toString().trim();
                    String role = etParti.getText().toString().trim();
                    if (TextUtils.isEmpty(prenom) || TextUtils.isEmpty(nom) || TextUtils.isEmpty(role)) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    user.setFullName(prenom + " " + nom);
                    user.setRole(role);
                    dbHelper.updateUser(user);
                    loadUsers();
                    Toast.makeText(getContext(), "Utilisateur modifié !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteUserDialog(User user) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Supprimer utilisateur")
                .setMessage("Voulez-vous vraiment supprimer cet utilisateur ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    dbHelper.deleteUser(user.getId());
                    loadUsers();
                    Toast.makeText(getContext(), "Utilisateur supprimé !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
} 