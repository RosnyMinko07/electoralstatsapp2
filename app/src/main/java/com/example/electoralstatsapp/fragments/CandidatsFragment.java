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
import com.example.electoralstatsapp.adapters.CandidatAdapter;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.Candidat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CandidatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CandidatAdapter adapter;
    private List<Candidat> candidatList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fab;
    private String userRole;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Uri selectedImageUri = null;
    private ImageView ivPhotoDialog;

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
        loadCandidats();

        fab.setOnClickListener(v -> showAddUserDialog());

        // Contrôle d'accès basé sur le rôle
        if (!"Administrateur".equals(userRole)) {
            fab.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView() {
        candidatList = new ArrayList<>();
        adapter = new CandidatAdapter(candidatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadCandidats() {
        if (adapter != null) {
            candidatList.clear();
            List<Candidat> all = dbHelper.getAllCandidats();
            SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String userEmail = prefs.getString("user_email", "");
            if ("superviseur".equals(userRole)) {
                for (Candidat c : all) {
                    if (userEmail.equals(c.getSaisiPar())) {
                        candidatList.add(c);
                    }
                }
            } else {
                candidatList.addAll(all);
            }
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
        ivPhotoDialog = dialogView.findViewById(R.id.dialog_iv_candidat_photo);

        ivPhotoDialog.setOnClickListener(v -> {
            if (checkAndRequestPermission()) {
                openGallery();
            }
        });

        builder.setView(dialogView)
                .setTitle("Nouveau candidat")
                .setPositiveButton("Ajouter", (dialog, id) -> {
                    String prenom = etPrenom.getText().toString().trim();
                    String nom = etNom.getText().toString().trim();
                    String parti = etParti.getText().toString().trim();
                    String bio = etBio.getText().toString().trim();
                    String photo = selectedImageUri != null ? selectedImageUri.toString() : "profilhomme";
                    SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    String userEmail = prefs.getString("user_email", "");
                    if (TextUtils.isEmpty(prenom) || TextUtils.isEmpty(nom) || TextUtils.isEmpty(parti)) {
                        Toast.makeText(getContext(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Candidat newCandidat = new Candidat();
                    newCandidat.setPrenom(prenom);
                    newCandidat.setNom(nom);
                    newCandidat.setParti(parti);
                    newCandidat.setBiographie(bio);
                    newCandidat.setPhoto(photo);
                    newCandidat.setSaisiPar(userEmail);
                    dbHelper.addCandidat(newCandidat);
                    loadCandidats();
                    Toast.makeText(getContext(), "Candidat ajouté !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean checkAndRequestPermission() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = android.Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{permission}, PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getContext(), "Permission refusée pour accéder à la galerie", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            if (ivPhotoDialog != null) {
                ivPhotoDialog.setImageURI(selectedImageUri);
            }
        }
    }
} 