package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.electoralstatsapp.MainActivity;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.User;

public class SignupFragment extends Fragment {

    private EditText fullNameEditText, emailEditText, passwordEditText;
    private Button signupButton;
    private TextView goToLoginTextView;
    private DatabaseHelper db;

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNav();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNav();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        db = new DatabaseHelper(getContext());
        fullNameEditText = view.findViewById(R.id.et_full_name);
        emailEditText = view.findViewById(R.id.et_email);
        passwordEditText = view.findViewById(R.id.et_password);
        signupButton = view.findViewById(R.id.btn_signup);
        goToLoginTextView = view.findViewById(R.id.tv_go_to_login);
        final View progressBar = view.findViewById(R.id.progress_signup_loading);

        signupButton.setOnClickListener(v -> {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            String fullName = fullNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            // Default role for new users, can be changed later
            String role = "électeur";

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                return;
            }

            User newUser = new User(fullName, email, password, role);
            long userId = db.addUser(newUser);

            if (userId != -1) {
                Toast.makeText(getContext(), "Inscription réussie", Toast.LENGTH_SHORT).show();
                // Navigate to LoginFragment after successful signup
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new LoginFragment());
                transaction.commit(); // Do not add to backstack to prevent going back to signup
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(getContext(), "L'inscription a échoué. L'email existe peut-être déjà.", Toast.LENGTH_LONG).show();
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
        });

        goToLoginTextView.setOnClickListener(v -> {
            // Navigate to LoginFragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new LoginFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
} 